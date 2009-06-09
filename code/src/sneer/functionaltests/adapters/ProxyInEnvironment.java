package sneer.functionaltests.adapters;

import static sneer.foundation.commons.environments.Environments.my;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sneer.foundation.commons.environments.Environment;
import sneer.foundation.commons.environments.Environments;
import sneer.foundation.commons.lang.ByRef;

final class ProxyInEnvironment<T> implements InvocationHandler {

	public static <T> T newInstance(final Class<T> intrface, final Environment environment) {
		final ByRef<T> result = ByRef.newInstance();
		Environments.runWith(environment, new Runnable() { @Override public void run() {
			final T delegate = my(intrface);
			final T proxy = newInstance(environment, delegate);
			result.value = proxy;
		}});
		return result.value;
	}

	private static <T> T newInstance(Environment environment, final T component) {
		final Class<? extends Object> componentClass = component.getClass();
		final ProxyInEnvironment<T> invocationHandler = new ProxyInEnvironment<T>(environment, component);
		return (T) Proxy.newProxyInstance(componentClass.getClassLoader(), componentClass.getInterfaces(), invocationHandler);
	}
	
	private final Environment _environment;
	private final T _delegate;

	private ProxyInEnvironment(Environment environment, T component) {
		_environment = environment;
		_delegate = component;
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		final ByRef<Object> result = ByRef.newInstance();

		Environments.runWith(_environment, new Runnable() { @Override public void run() {
			try {
				result.value = method.invoke(_delegate, args);
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException();
			} catch (IllegalAccessException e) {
				throw new IllegalStateException();
			} catch (InvocationTargetException e) {
				result.value = e.getCause();
			}
		}});
		
		if (result.value == null)
			return null;
		
		if (result.value instanceof Throwable)
			throw (Throwable)result.value;
		
		return result.value;
	}

}