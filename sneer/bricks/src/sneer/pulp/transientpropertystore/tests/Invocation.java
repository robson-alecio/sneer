package sneer.pulp.transientpropertystore.tests;

import java.lang.reflect.Method;
import java.util.Date;
import static sneer.commons.environments.Environments.my;
import org.prevayler.TransactionWithQuery;

import wheel.lang.FrozenTime;

class Invocation implements TransactionWithQuery {

//	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private final Method _method;
	private final Object[] _args;
	private final Class<?> _brick;

	public Invocation(Class<?> brick, Method method, Object[] args) {
		_brick = brick;
		_method = method;
		_args = args;
	}


	public Object executeAndQuery(Object stateMachine, Date date) throws Exception {
		FrozenTime.freezeForCurrentThread(date.getTime());
		try {
			return ((MethodInvoker)stateMachine).invoke(brickImpl(),  _method, _args);
		} catch (Throwable e) {
			if (e instanceof Error) throw (Error)e;
			if (e instanceof Exception) throw (Exception)e;
			throw new Exception(e);
		}
	}


	private Object brickImpl() {
		return ((BrickProxy)my(_brick)).brickImpl();
	}
	
}
