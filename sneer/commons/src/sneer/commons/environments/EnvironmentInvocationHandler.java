package sneer.commons.environments;

import static sneer.commons.environments.Environments.my;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sneer.commons.lang.ByRef;

final class EnvironmentInvocationHandler<T> implements InvocationHandler {

	public static <T> T newInstance(final Environment environment, final Class<T> intrface) {
		final ByRef<T> result = ByRef.newInstance();
		Environments.runWith(environment, new Runnable() { @Override public void run() {
			final T component = my(intrface);
			final T proxy = newInstance(environment, component);
			result.value = proxy;
		}});
		return result.value;
	}

	public static <T> T newInstance(Environment environment, final T component) {
		final Class<? extends Object> componentClass = component.getClass();
		final EnvironmentInvocationHandler<T> invocationHandler = new EnvironmentInvocationHandler<T>(environment, component);
		return (T) Proxy.newProxyInstance(componentClass.getClassLoader(), componentClass.getInterfaces(), invocationHandler);
	}
	
	private final Environment _environment;
	private final T _component;

	private EnvironmentInvocationHandler(Environment environment, T component) {
		_environment = environment;
		_component = component;
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		final ByRef<Object> result = ByRef.newInstance();

		Environments.runWith(_environment, new Runnable() { @Override public void run() {
			try {
				result.value = method.invoke(_component, args);
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