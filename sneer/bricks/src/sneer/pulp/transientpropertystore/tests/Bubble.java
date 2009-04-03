package sneer.pulp.transientpropertystore.tests;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.prevayler.Prevayler;

public class Bubble {

	public static <STATE_MACHINE> STATE_MACHINE wrapStateMachine(Prevayler prevayler) {
		Object stateMachine = prevayler.prevalentSystem();
		return (STATE_MACHINE)wrap(stateMachine, prevayler);
	}

	private static <T> T wrap(Object object, Prevayler prevayler) {
		InvocationHandler handler = new Bubble(prevayler).handler();
		return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), handler);
	}

	private Bubble(Prevayler prevayler) {
		_prevayler = prevayler;
	}
	
	private final Prevayler _prevayler;
	
	
	private InvocationHandler handler() {
		return new InvocationHandler() {
			@Override
			public Object invoke(Object proxyImplied, Method method, Object[] args) throws Throwable {
				return _prevayler.execute(new Invocation(method, args));
			}
		};
	}

}
