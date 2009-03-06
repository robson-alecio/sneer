package sneer.commons.environments;

import static sneer.commons.environments.Environments.my;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sneer.commons.environments.Environments.Memento;
import sneer.commons.lang.ByRef;


final class EnvironmentInvocationHandler<T> implements InvocationHandler {

	public static <T> T newInstance(Environment environment, final Class<T> intrface) {
		final ByRef<T> result = ByRef.newInstance();
		Environments.runWith(environment, new Runnable() { @Override public void run() {
			final T component = my(intrface);
			final Class<? extends Object> componentClass = component.getClass();
			result.value = (T) Proxy.newProxyInstance(componentClass.getClassLoader(), componentClass.getInterfaces(), new EnvironmentInvocationHandler<T>(Environments.memento(), component));
		}});
		return result.value;
	}
	
	private final Memento _memento;
	private final T _component;

	private EnvironmentInvocationHandler(Memento memento, T component) {
		_memento = memento;
		_component = component;
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		final ByRef<Object> result = ByRef.newInstance();

		Environments.runWith(_memento, new Runnable() { @Override public void run() {
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