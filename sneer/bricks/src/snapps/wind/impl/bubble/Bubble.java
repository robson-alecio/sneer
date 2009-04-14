package snapps.wind.impl.bubble;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.prevayler.Prevayler;

import sneer.software.lang.Consumer;
import sneer.software.lang.PickyConsumer;


public class Bubble {

	public static <STATE_MACHINE> STATE_MACHINE wrapStateMachine(Prevayler prevayler) {
		Object stateMachine = prevayler.prevalentSystem();
		return (STATE_MACHINE)wrap(stateMachine, prevayler, new ArrayList<String>());
	}

	private static <T> T wrap(Object object, Prevayler prevayler, List<String> getterMethodPath) {
		InvocationHandler handler = new Bubble(object, prevayler, getterMethodPath).handler();
		Object proxy = Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), handler);
		return (T)proxy;
	}

	private Bubble(Object stateMachine, Prevayler prevayler, List<String> getterMethodPath) {
		_stateMachine = stateMachine;
		_prevayler = prevayler;
		_getterMethodPath = getterMethodPath;
	}
	
	private final Object _stateMachine;
	private final Prevayler _prevayler;
	private final List<String> _getterMethodPath;
	
	
	private InvocationHandler handler() {
		return new InvocationHandler() {
			@Override
			public Object invoke(Object proxyImplied, Method method, Object[] args) throws Throwable {
				Object result = invokeOnStateMachine(method, args);
				return wrapIfNecessary(result, method);
			}
		};
	}
	
	private Object invokeOnStateMachine(Method method, Object[] args) throws Throwable {
		Object result;
		try {
			result = method.invoke(_stateMachine, args);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
		return result;
	}

	
	private Object wrapIfNecessary(Object object, Method method) {
		String methodName = method.getName();
		if (methodName.equals("output")) return object;
		
		List<String> pathToObject = new ArrayList<String>(_getterMethodPath.size() + 1);
		pathToObject.addAll(_getterMethodPath);
		pathToObject.add(methodName);

		Class<?> type = method.getReturnType();
		if (Consumer.class.isAssignableFrom(type))
			return new ConsumerBubble(_prevayler, pathToObject);
		if (PickyConsumer.class.isAssignableFrom(type))
			return new PickyConsumerBubble(_prevayler, pathToObject);
		
		if (type == Void.class) throw new UnsupportedOperationException("Void methods such as " + method + " are not supported.");
		
		return wrap(object, _prevayler, pathToObject);
	}

}
