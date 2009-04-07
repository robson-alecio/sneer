package sneer.pulp.transientpropertystore.tests;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.prevayler.Prevayler;

public class Bubble {

	public static <STATE_MACHINE> STATE_MACHINE wrapStateMachine(Class<?> brick, Object brickImpl, Prevayler prevayler) {
		return (STATE_MACHINE)wrap(brick, brickImpl, prevayler);
	}

	private static <T> T wrap(Class<?> brick, Object brickImpl, Prevayler prevayler) {
		InvocationHandler handler = new Bubble(brick, brickImpl, prevayler).handler();
		return (T) Proxy.newProxyInstance(brick.getClassLoader(), interfaces(brick), handler);
	}

	private static Class<?>[] interfaces(Class<?> brick) {
		return new Class[]{brick, BrickProxy.class};
	}

	private Bubble(Class<?> brick, Object brickImpl, Prevayler prevayler) {
		_brick = brick;
		_brickImpl = brickImpl;
		_prevayler = prevayler;
	}
	
	private Class<?> _brick;
	private final Object _brickImpl;
	private final Prevayler _prevayler;
	static private boolean _insidePrevayler = false;
	
	
	private InvocationHandler handler() {
		return new InvocationHandler() { @Override public Object invoke(Object proxyImplied, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("brickImpl"))
				return _brickImpl;
			
			if (_insidePrevayler) {
				return Invocation.invoke(_brickImpl,  method, args);
			}
			
			_insidePrevayler = true;
			try {
				return _prevayler.execute(new Invocation(_brick, method, args));
			} finally {
				_insidePrevayler = false;
			}
		}};
	}

}
