package sneer.tests.adapters;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.ByRef;

final class ProxyInEnvironment implements InvocationHandler {

	public static Object newInstance(Environment environment, final Object component) {
		final Class<? extends Object> componentClass = component.getClass();
		final ProxyInEnvironment invocationHandler = new ProxyInEnvironment(environment, component);
		return Proxy.newProxyInstance(componentClass.getClassLoader(), componentClass.getInterfaces(), invocationHandler);
	}
	
	private final Environment _environment;
	private final Object _delegate;

	private ProxyInEnvironment(Environment environment, Object component) {
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