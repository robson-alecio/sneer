package sneer.kernel.gui;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.prevayler.Prevayler;

import sneer.kernel.business.BusinessSource;
import wheel.lang.Consumer;

public class PersistenceHandler implements InvocationHandler {

	private final Object _delegate;
	private final Prevayler _prevayler;

	public PersistenceHandler(Object delegate, Prevayler prevayler) {
		_delegate = delegate;
		_prevayler = prevayler;
	}

	public Object invoke(Object proxyImplied, Method method, Object[] args) {
		Object result;
		try {
			result = method.invoke(_delegate, args);
		} catch (InvocationTargetException e) {
			throw (RuntimeException)e.getCause();
		} catch (Exception c) {
			throw new IllegalStateException(c);
		}
		
		if (Consumer.class.isAssignableFrom(method.getReturnType())) {
			return wrap(result, method.getName());
		}
		return result;
	}

	private Object wrap(Object obj, String methodName) {
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), 
				obj.getClass().getInterfaces() , new ConsumerPersistenceHandler(_prevayler, methodName)); //Fix: do not use all interfaces.
	}

	@SuppressWarnings("unchecked")
	public static <PREVALENT_SYSTEM> PREVALENT_SYSTEM persistentProxyFor(Prevayler prevayler) {
		Object instance = prevayler.prevalentSystem();
		return (PREVALENT_SYSTEM)Proxy.newProxyInstance(instance.getClass().getClassLoader(), instance.getClass().getInterfaces(), new PersistenceHandler(instance, prevayler));
	}

}
