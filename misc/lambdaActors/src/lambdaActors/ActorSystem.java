package lambdaActors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class ActorSystem {

	private static class ActorInfo<T> {

		private final T actorImpl;
		private final Object data;

		public ActorInfo(T actorImpl, Object data) {
			this.actorImpl = actorImpl;
			this.data = data;
		}

	}

	private static Map<Class<? extends ActorImpl>, Map<Object,ActorInfo<?>>> actorDefinitions;

	private static AtomicLong count = new AtomicLong();

	private static ExecutorService tPool;

	static {
		actorDefinitions = new HashMap<>();
		tPool = Executors.newFixedThreadPool(1);
	}

	static <T extends ActorImpl> void deregister(T actorImpl, Object identifier) {
		Map<Object,ActorInfo<?>> actorMap = actorDefinitions.get(actorImpl.getClass());
		ActorInfo<?> removed = actorMap.remove(identifier);
		if (removed != null) {
			if (count.decrementAndGet() == 0) {
				tPool.shutdown();
			}
		}
	}

	static <T extends ActorImpl> Object register(T actorImpl, Object data) {
		Map<Object, ActorInfo<?>> actors = actorDefinitions.get(actorImpl.getClass());
		if (actors == null) {
			synchronized (actorDefinitions) {
				actors = actorDefinitions.get(actorImpl.getClass());
				if (actors == null) {
					actors = new HashMap<>();
					actorDefinitions.put(actorImpl.getClass(), actors);
				}
			}
		}
		if (count.incrementAndGet() == 1) {
			
		}
		Object identifier = actorImpl.getIdentifier();
		actors.put(identifier,new ActorInfo(actorImpl,data));
		return identifier;
	}

	static void submit(Runnable task) {
		tPool.submit(task);
	}

	private static enum State {
		NEW, RESULT, EXCEPTION
	}

	private static class ActorCallInformation {

		private CompletableFuture<?> future;
		private Object result;
		private Throwable exception;
		private State state;
		private Object data;

		public ActorCallInformation() {
			state = State.NEW;
		}
	}

	public static class SuppressedResultException extends Exception {

		private Object value;

		public SuppressedResultException(Object value, Throwable suppressed) {
			super("SuppressingValue:" + value);
			this.value = value;
			addSuppressed(suppressed);
		}

		@Override
		public String toString() {
			return super.toString() + " SuppressingValue:" + value;
		}

	}

	private static ThreadLocal<ActorCallInformation> actorCall = new ThreadLocal<>();

	public static Future<Void> nothing() {
		return value(null);
	}

	static void complete(Object staticResult) {
		ActorCallInformation aci = actorCall.get();
		switch (aci.state) {
		case NEW:
			completeValue(staticResult);
			break;
		case RESULT:
			completeValue(aci.result);
			break;
		case EXCEPTION:
			completeThrownException(aci.exception);
			break;
		default:
			throw new RuntimeException("Nothing known about State " + aci.state);
		}
		actorCall.remove();
	}

	public static <T> Future<T> value(T value) {
		ActorCallInformation aci = actorCall.get();
		if (aci.state != State.NEW) {
			throw new RuntimeException(aci.state + " already known");
		}
		aci.state = State.RESULT;
		aci.result = value;
		return (Future<T>) aci.future;
	}

	private static <T> Future<T> completeValue(T value) {
		CompletableFuture<T> future = (CompletableFuture<T>) actorCall.get().future;
		future.complete(value);
		actorCall.remove();
		return future;
	}

	public static <T> Future<T> thrownException(Throwable th) {
		ActorCallInformation aci = actorCall.get();
		if (aci.state != State.NEW) {
			throw new RuntimeException(aci.state + " already known");
		}
		aci.state = State.EXCEPTION;
		actorCall.get().exception = th;
		return (Future<T>) aci.future;
	}

	private static <T> Future<T> completeThrownException(Throwable th) {
		CompletableFuture<T> future = (CompletableFuture<T>) actorCall.get().future;
		future.completeExceptionally(th);
		actorCall.remove();
		return future;
	}

	static Future<?> prepareFuture(CompletableFuture<?> fut) {
		actorCall.set(new ActorCallInformation());
		actorCall.get().future = fut;
		return fut;
	}

	static void completeWithAdditionalException(Throwable th) {
		ActorCallInformation aci = actorCall.get();
		switch (aci.state) {
		case EXCEPTION:
			th.addSuppressed(aci.exception);
			completeThrownException(th);
			break;
		case RESULT:
			completeThrownException(new SuppressedResultException(aci.result,
					th));
			break;
		case NEW:
			completeThrownException(th);
			break;
		}
	}

	public static Future<?> getFuture() {
		return actorCall.get().future;
	}

	public static Object getData() {
		return actorCall.get().data;
	}

	public static void prepareData(Class<? extends ActorImpl> actorImpl,Object identifier) {
		Map<Object, ActorInfo<?>> map = actorDefinitions.get(actorImpl);
		ActorInfo<?> actorInfo = map.get(identifier);
		actorCall.get().data = actorInfo.data;
	}

}
