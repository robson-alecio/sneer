package sneer.pulp.mesh.impl;

import static wheel.lang.Types.cast;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import sneer.lego.Brick;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Register;
import wheel.reactive.RegisterBase;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.impl.ListRegisterImpl;
import wheel.reactive.sets.SetRegister;
import wheel.reactive.sets.SetSignal;
import wheel.reactive.sets.SetSignal.SetValueChange;
import wheel.reactive.sets.impl.SetRegisterImpl;


public class BrickProxy implements InvocationHandler {

	static public <B extends Brick> B createFor(Class<B> brickInterface, SignalPublisher intermediary) {
		return cast(
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

	private final Map<String, RegisterBase> _registersBySignalName = new HashMap<String, RegisterBase>();

	private final Class<? extends Brick> _brickInterface;
	private final SignalPublisher _publisher;


	@Override
	public Object invoke(Object impliedProxy, Method method, Object[] args)	throws Throwable {
		if (method.getDeclaringClass() == Object.class)
			return method.invoke(this, args);
		
		if (args != null) throw new NotImplementedYet();

		Class<?> signalType = method.getReturnType();
		return signal(method.getName(), signalType);
	}

	private Object signal(String signalName, Class<?> type) {
		RegisterBase register = produceRegisterFor(signalName, type);
		return register.output();   //Fix: Signal type mismatch between peers is possible. 
	}

	private RegisterBase produceRegisterFor(String signalName, Class<?> type) {
		RegisterBase register = _registersBySignalName.get(signalName);
		if (register == null) {
			register = createRegisterFor(type);
			_registersBySignalName.put(signalName, register);
			subscribeTo(signalName);
		}
		return cast(register);
	}

	
	
	private RegisterBase createRegisterFor(Class<?> type) {
		if (Signal.class.isAssignableFrom(type))
			return new RegisterImpl<Object>(null);
		if (ListSignal.class.isAssignableFrom(type))
			return new ListRegisterImpl<Object>();
		if (SetSignal.class.isAssignableFrom(type))
			return new SetRegisterImpl<Object>();
		
		throw new UnsupportedOperationException();
	}

	private void subscribeTo(String signalName) {
		_publisher.subscribeTo(_brickInterface, signalName);
	}

	void handleNotification(String signalName, Object notification) {
		RegisterBase register = _registersBySignalName.get(signalName);
		if (register == null) return;
		
		if (handleListNotification(register, notification)) return;
		if (handleSetNotification(register, notification)) return;
		
		Register<Object> casted = cast(register);
		casted.setter().consume(notification);			
	}

	private boolean handleListNotification(RegisterBase register, Object notification) {
		if (!(notification instanceof ListValueChange)) return false;
		
		register.toString();
		throw new UnsupportedOperationException(); //Implement see handleSetNotification()
	}

	private boolean handleSetNotification(RegisterBase register, Object notification) {
		if (!(notification instanceof SetValueChange)) return false;
		
		SetRegister<Object> castedRegister = cast(register);
		SetValueChange<Object> castedNotification = cast(notification);

		castedRegister.change(castedNotification);
		return true;
	}


}
