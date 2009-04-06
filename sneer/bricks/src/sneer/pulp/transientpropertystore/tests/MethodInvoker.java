package sneer.pulp.transientpropertystore.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MethodInvoker {

	public Object invoke(Object target, Method method, Object[] args) throws Throwable {
		try {
			//candidate.getClass().getMethod(getterName, new Class[0]);
			return  method.invoke(target, args);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
}
