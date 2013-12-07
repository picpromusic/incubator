package lambdaActors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public abstract class ActorImpl<ACTOR_INTERFACE, DATA> implements AutoCloseable {

	protected static Object getData() {
		return ActorSystem.getData();
	}

	private Class<ACTOR_INTERFACE> actorInterface;
	private Map<Class<? super ACTOR_INTERFACE>, SamHolder> messageReceivers;
	private List<SamCall> mailbox;
	private HashSet<Object> ownActors;
	private static AtomicLong nextActor;

	static {
		nextActor = new AtomicLong();
	}

	public ActorImpl(Class<ACTOR_INTERFACE> actorInterface) {
		this.actorInterface = actorInterface;
		this.messageReceivers = buildMessageReceiverMap();
		this.ownActors = new HashSet<>();
	}

	protected Map<Class<? super ACTOR_INTERFACE>, SamHolder> buildMessageReceiverMap() {
		return new HashMap<>();
	}

	public ACTOR_INTERFACE createNew() {
		final Object identifier = ActorSystem.register(this, createNewData());
		ownActors.add(identifier);
		
		return (ACTOR_INTERFACE) Proxy.newProxyInstance(
				actorInterface.getClassLoader(),
				new Class[] { actorInterface },
				(Object proxy, Method method, Object[] args) -> {
					SamHolder samHolder = messageReceivers.get(method
							.getDeclaringClass());
					if (samHolder == null) {
						throw new IllegalStateException("No definition for "
								+ method);
					}
					CompletableFuture<?> fut;
					SamCall<Object> task = new SamCall<>(samHolder, identifier,
							args, this.getClass());
					ActorSystem.submit(task);
					return task.getFuture();
				});
	}

	protected void bake() {
		Class<?>[] interfaces = actorInterface.getInterfaces();
		try {
			for (Method method : this.getClass().getDeclaredMethods()) {
				ActorMessage annotation = method
						.getAnnotation(ActorMessage.class);
				if (annotation != null) {
					Class<?> actorMessageType = annotation.value();
					if (Object.class.equals(actorMessageType)) {
						Object o = method.invoke(this, (Object[]) null);
						Method f = o.getClass().getDeclaredMethods()[0];
						Class<? super ACTOR_INTERFACE> samInterface = (Class<? super ACTOR_INTERFACE>) f
								.getDeclaringClass().getInterfaces()[0];
						messageReceivers.put(samInterface, new SamHolder(o, f));
					} else if (Modifier.isStatic(method.getModifiers())) {
						Method[] declaredMethods = actorMessageType
								.getDeclaredMethods();
						if (declaredMethods.length > 1) {
							throw new RuntimeException(actorMessageType
									+ " is not a SAM-Type");
						}
						Class<?>[] interfaceTypes = declaredMethods[0]
								.getParameterTypes();
						Class<?>[] receivingTypes = method.getParameterTypes();
						if (interfaceTypes.length != receivingTypes.length) {
							throw new RuntimeException("Count of types in "
									+ declaredMethods[0]
									+ " do not match the types in " + method);
						}
						for (int i = 0; i < receivingTypes.length; i++) {
							receivingTypes[i]
									.isAssignableFrom(interfaceTypes[i]);
						}
						messageReceivers
								.put((Class<? super ACTOR_INTERFACE>) actorMessageType,
										new SamHolder<>(null, method));
					} else {
						throw new RuntimeException(
								"ActorMessage annotated methods with explicit ActorInterface definition need to be static.");
					}
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static class SamHolder<T> {
		private Object o;
		private Method f;

		public SamHolder(Object o, Method f) {
			this.o = o;
			this.f = f;
			this.f.setAccessible(true);
		}

		public Object call(Object[] params) throws IllegalAccessException,
				IllegalArgumentException, InvocationTargetException {
			return f.invoke(o, params);

		}
	}

	private static class SamCall<T> implements Runnable {
		private SamHolder<T> sh;
		private Object[] params;
		private CompletableFuture<?> fut;
		private Object identifier;
		private Class<? extends ActorImpl> actorImpl;

		public SamCall(SamHolder<T> sh, Object identifier, Object[] params,
				Class<? extends ActorImpl> actorImpl) {
			this.sh = sh;
			this.params = params;
			this.identifier = identifier;
			this.actorImpl = actorImpl;

			fut = new CompletableFuture<>();
		}

		public Future<?> getFuture() {
			return fut;
		}

		@Override
		public void run() {
			ActorSystem.prepareFuture(fut);
			ActorSystem.prepareData(actorImpl, identifier);
			try {
				Object result = sh.call(params);
				ActorSystem.complete(result);
			} catch (InvocationTargetException tex) {
				ActorSystem.completeWithAdditionalException(tex
						.getTargetException());
			} catch (Throwable th) {
				ActorSystem.completeWithAdditionalException(th);
				return;
			}
		}
	}

	@Override
	public void close() {
		for (Object identifier : ownActors) {
			ActorSystem.deregister(this, identifier);
		}
	}

	public Object getIdentifier() {
		return getNextActorNum();
	}

	protected long getNextActorNum() {
		return nextActor.incrementAndGet();
	}

	public void registeredAs(Object key) {
		this.ownActors.add(key);
	}

	protected abstract Object createNewData();
}
