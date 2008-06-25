package sneer.bricks.mesh.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import sneer.lego.Brick;
import wheel.lang.Casts;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;


class BrickProxy implements InvocationHandler {

	static <B extends Brick> B createFor(Class<B> brickInterface, SignalPublisher intermediary) {
		return Casts.uncheckedGenericCast(
			Proxy.newProxyInstance(
				BrickProxy.class.getClassLoader(),
				new Class<?>[]{ brickInterface },
				new BrickProxy(brickInterface, intermediary)));
	}
	
	static void handleNotification(Brick brick, String signalName, Object notification) {
		BrickProxy handler = (BrickProxy)Proxy.getInvocationHandler(brick);
		handler.handleNotification(signalName, notification);
	}

	
	private <B extends Brick> BrickProxy(Class<B> brickInterface, SignalPublisher publisher) {
		_brickInterface = brickInterface;
		_publisher = publisher;
	}

	private final Map<String, Register<Object>> _registersBySignalName = new HashMap<String, Register<Object>>();

	private final Class<? extends Brick> _brickInterface;
	private final SignalPublisher _publisher;


	@Override
	public Object invoke(Object impliedProxy, Method method, Object[] args)	throws Throwable {
		if (args != null) throw new NotImplementedYet();
		
		Class<?> returnType = method.getReturnType();
		
		if (!Signal.class.isAssignableFrom(returnType)) throw new NotImplementedYet();

		return signal(method.getName());
	}

	private <S> Signal<S> signal(String signalName) {
		Register<S> register = produceRegisterFor(signalName);
		return register.output();   //Fix: Signal type mismatch between peers is possible. 
	}

	private <T> Register<T> produceRegisterFor(String signalName) {
		Register<Object> register = _registersBySignalName.get(signalName);
		if (register == null) {
			register = new RegisterImpl<Object>(null);
			_registersBySignalName.put(signalName, register);
			subscribeTo(signalName);
		}
		return Casts.uncheckedGenericCast(register);
	}

	private void subscribeTo(String signalName) {
		_publisher.subscribeTo(_brickInterface, signalName);
	}

	void handleNotification(String signalName, Object notification) {
		produceRegisterFor(signalName).setter().consume(notification);
	}


}
