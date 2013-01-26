package com.wordpress.codingwizard.mvp.tools;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.UISynchronize;

@Creatable
public class MVPManager {

	@Inject
	UISynchronize uisync;

	/**
	 * Creates a Presenter for the View and Links the Presenter to the View
	 * without any delay
	 * 
	 * @param view
	 *            The View
	 * @param ctx
	 *            The Context of the View
	 * @param desiredType
	 *            The Type of the Presenter
	 */
	public void linkPresenter(final Object view, final IEclipseContext ctx,
			final Class<?> desiredType) {
		createLinkPresenterJob(view, ctx, desiredType).schedule();
	}

	/**
	 * Creates a Presenter for the View and Links the Presenter to the View with
	 * the given delay
	 * 
	 * @param view
	 *            The View
	 * @param ctx
	 *            The Context of the View
	 * @param desiredType
	 *            The Type of the Presenter
	 * @param delay
	 *            in ms
	 */
	public void linkPresenter(final Object view, final IEclipseContext ctx,
			final Class<?> desiredType, long delay) {
		createLinkPresenterJob(view, ctx, desiredType).schedule(delay);
	}

	/**
	 * Creates a Job which creates a Presenter for the View and links the
	 * Presenter to the View with
	 * 
	 * @param view
	 *            The View
	 * @param ctx
	 *            The Context of the View
	 * @param desiredType
	 *            The Type of the Presenter
	 * @param delay
	 *            in ms
	 */
	private Job createLinkPresenterJob(final Object view,
			final IEclipseContext ctx, final Class<?> desiredType) {
		Job j = new Job(getClass().getName() + "/linkPresenter") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// Search for View Annotation of the desired PresenterClass and
				// retrive Display-Interface
				View viewAnnotation = (View) desiredType
						.getAnnotation(View.class);
				Class viewInterface = viewAnnotation.display();
				if (viewInterface.isInterface()) {
					// Create Proxy Class for the View-Instance that is
					// executing all Methods in the UI-Thread
					Object uiThreadProxy = Proxy.newProxyInstance(
							viewInterface.getClassLoader(),
							new Class[] { viewInterface }, uiThread(view));

					// Create a new SubContext and insert the given view as
					// implementation for the display Interface for injection
					// into the presenter
					IEclipseContext childCtx = ctx.createChild("presenter");
					childCtx.set(viewInterface, uiThreadProxy);
					ContextInjectionFactory.make(desiredType, childCtx);

					return Status.OK_STATUS;
				} else {
					throw new IllegalArgumentException("Display "
							+ viewInterface + " given in View Annotaion of "
							+ desiredType + " is not an interface. It is an "
							+ Modifier.toString(viewInterface.getModifiers()));
				}
			}
		};
		j.setSystem(true);
		return j;
	}

	/**
	 * Creates an InvocationHandler that executes every Method inside the
	 * UI-Thread
	 * 
	 * @param callingTarget
	 *            targetObjet that should be called only in the uiThread
	 * @return the InvocationHandler
	 */
	private InvocationHandler uiThread(final Object callingTarget) {
		return new InvocationHandler() {

			@Override
			public Object invoke(Object object, final Method method,
					Object[] args) throws Throwable {
				final List<Object> ret = new ArrayList<>(1);
				final List<Throwable> ex = new ArrayList<>(1);
				final Object[] wrappedArgs = wrapArguments(
						method.getParameterTypes(), args);
				Runnable run = new Runnable() {

					@Override
					public void run() {
						try {

							ret.add(method.invoke(callingTarget, wrappedArgs));
						} catch (Throwable e) {
							ex.add(e);
							throw new RuntimeException(e);
						}
					}
				};
				if (!method.getReturnType().equals(void.class)) {
					// FIXME: FutureWrapper to handle Deadlock
					// Situation
					uisync.syncExec(run);
					// Display.getCurrent().syncExec(run);
					if (ex.isEmpty()) {
						return ret.get(0);
					} else {
						throw (Throwable) ex.get(0);
					}
				} else {
					uisync.asyncExec(run);
					return null;
				}
			}

		};
	}

	/**
	 * Wrap every method argument that is of an interface type with an Proxy so
	 * that every Method Call on the Arguments is executed outside the uiThread
	 * 
	 * @param types
	 *            of the Method arguments
	 * @param args
	 *            arguments
	 * 
	 * @return
	 */
	protected Object[] wrapArguments(Class<?>[] types, Object[] args) {
		for (int i = 0; i < types.length; i++) {
			// If type is an Interface the Argument can be wrapped by an Proxy
			if (types[i].isInterface()) {
				args[i] = wrapArgument(args[i], types[i]);
			}
		}
		return args;
	}

	/**
	 * Wraps a single argument to the given interface type. Every Method-call
	 * to the wrapped argument is executed outside the ui-thread
	 * 
	 * @param argument
	 *            the argument to wrap
	 * @param type
	 *            the interface typ to wrap to
	 * @return
	 */
	private Object wrapArgument(final Object argument, Class<?> type) {
		return Proxy.newProxyInstance(type.getClassLoader(),
				new Class<?>[] { type }, new InvocationHandler() {

					@Override
					public Object invoke(final Object object, final Method m,
							final Object[] args) throws Throwable {
						final List<Object> ret = new ArrayList<>(1);
						final List<Throwable> ex = new ArrayList<>(1);
						Job j = new Job(getClass().getName() + "/leaveUIThread") {

							@Override
							protected IStatus run(IProgressMonitor monitor) {
								try {
									ret.add(m.invoke(argument, args));
									return Status.OK_STATUS;
								} catch (Throwable e) {
									ex.add(e);
									throw new RuntimeException(e);
								}
							}

						};
						j.setSystem(true);
						j.schedule();
						if (!m.getReturnType().equals(void.class)) {
							// FIXME: FutureWrapper to handle Deadlock
							// Situation
							j.join();
							if (ex.isEmpty()) {
								return ret.get(0);
							} else {
								throw (Throwable) ex.get(0);
							}
						} else {
							return null;
						}
					}

				});
	}

}
