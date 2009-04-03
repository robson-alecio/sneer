package sneer.pulp.transientpropertystore.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.prevayler.TransactionWithQuery;

import wheel.lang.FrozenTime;

class Invocation implements TransactionWithQuery {

//	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private final Method _method;
	private final Object[] _args;

	public Invocation(Method method, Object[] args) {
		_method = method;
		_args = args;
	}


	public Object executeAndQuery(Object stateMachine, Date date) throws Exception {
		FrozenTime.freezeForCurrentThread(date.getTime());
		try {
			return invokeOnStateMachine(stateMachine, _method, _args);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	private Object invokeOnStateMachine(Object stateMachine, Method method, Object[] args) throws Throwable {
		Object result;
		try {
			//candidate.getClass().getMethod(getterName, new Class[0]);
			result = method.invoke(stateMachine, args);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
		return result;
	}

	private static final long serialVersionUID = 1L;

}
