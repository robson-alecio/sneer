package prevayler.bubble;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.prevayler.Prevayler;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;

public class Bubble implements InvocationHandler {

	@SuppressWarnings("unchecked")
	public static <STATE_MACHINE> STATE_MACHINE wrapStateMachine(Prevayler prevayler) {
		Object stateMachine = prevayler.prevalentSystem();
		Bubble handler = new Bubble(stateMachine, prevayler);
		Object proxy = Proxy.newProxyInstance(stateMachine.getClass().getClassLoader(), stateMachine.getClass().getInterfaces(), handler);
		return (STATE_MACHINE)proxy;  //Refactor Remove this cast and use Casts.uncheckedCast() instead, when the Sun compiler can handle it (bug fixed in JDK7). Remove the @SuppressWarnings for this method.
	}

	
	private Bubble(Object stateMachine, Prevayler prevayler) {
		_stateMachine = stateMachine;
		_prevayler = prevayler;
	}

	
	private final Object _stateMachine;
	private final Prevayler _prevayler;

	
	public Object invoke(Object proxyImplied, Method method, Object[] args) throws InvocationTargetException {
		Object result = invoke(method, args);
		return wrapIfNecessary(result, method);
	}

	
	private Object invoke(Method method, Object[] args) throws InvocationTargetException {
		Object result;
		try {
			result = method.invoke(_stateMachine, args);
		} catch (InvocationTargetException e) {
			throw e;
		} catch (Exception c) {
			throw new IllegalStateException(c);
		}
		return result;
	}

	
	private Object wrapIfNecessary(Object object, Method method) {
		Class<?> type = method.getReturnType();
		String methodName = method.getName();
		
		if (Omnivore.class.isAssignableFrom(type))
			return new OmnivoreBubble(_prevayler, methodName);
		if (Consumer.class.isAssignableFrom(type))
			return new ConsumerBubble(_prevayler, methodName);
		
		if (!methodName.equals("output")) throw new IllegalStateException();
			
		return object;
	}

}
