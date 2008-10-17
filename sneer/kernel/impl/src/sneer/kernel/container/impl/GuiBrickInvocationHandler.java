/**
 * 
 */
package sneer.kernel.container.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import wheel.io.ui.GuiThread;
import wheel.lang.ByRef;

final class GuiBrickInvocationHandler<T> implements InvocationHandler {

	public static <T> T decorate(final T component) {
		final Class<? extends Object> componentClass = component.getClass();
		return (T) Proxy.newProxyInstance(componentClass.getClassLoader(), componentClass.getInterfaces(), new GuiBrickInvocationHandler<T>(component));
	}
	
	private final T _component;

	private GuiBrickInvocationHandler(T component) {
		_component = component;
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		final ByRef<Object> result = ByRef.newInstance();

		GuiThread.invokeAndWait(new Runnable() { @Override public void run() {
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