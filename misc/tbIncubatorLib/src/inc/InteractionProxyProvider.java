package inc;

import com.google.inject.Provider;

public class InteractionProxyProvider<T> implements Provider<T> {

	private Class<? extends T> impl;

	public InteractionProxyProvider(Class<? extends T> impl) {
		this.impl = impl;
	}

	@Override
	public T get() {
		return InteractionProxy.build(impl);
	}

}
