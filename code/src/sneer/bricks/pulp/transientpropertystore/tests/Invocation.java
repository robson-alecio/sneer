package sneer.bricks.pulp.transientpropertystore.tests;

import static sneer.foundation.commons.environments.Environments.my;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.prevayler.TransactionWithQuery;


class Invocation implements TransactionWithQuery {

	private final Method _method;
	private final Object[] _args;
	private final Class<?> _brick;

	public Invocation(Class<?> brick, Method method, Object[] args) {
		_brick = brick;
		_method = method;
		_args = args;
	}


	public Object executeAndQuery(Object stateMachineIgnored, Date date) throws Exception {
		try {
			return invoke(brickImpl(), _method, _args);
		} catch (Throwable e) {
			e.printStackTrace();
			if (e instanceof Error) throw (Error)e;
			if (e instanceof Exception) throw (Exception)e;
			throw new Exception(e);
		}
	}


	private Object brickImpl() {
		return ((BrickProxy)my(_brick)).brickImpl();
	}
	
	static Object invoke(Object target, Method method, Object[] args) throws Throwable {
		try {
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
