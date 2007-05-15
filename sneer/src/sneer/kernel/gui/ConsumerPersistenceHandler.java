package sneer.kernel.gui;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.prevayler.Prevayler;

public class ConsumerPersistenceHandler implements InvocationHandler { //Refactor: consider adding "implements Consumer" to be warned by the compiler when the definition of the interface changes.

	private final String _consumerGetterMethodName;
	private final Prevayler _prevayler;

	public ConsumerPersistenceHandler(Prevayler prevayler, String consumerGetterMethodName) {
		_consumerGetterMethodName = consumerGetterMethodName;
		_prevayler = prevayler;
	}

	public Object invoke(Object proxyImplied, Method methodImplied, Object[] args)	throws Throwable {
		if(!methodImplied.getName().equals("consume")) throw new UnsupportedOperationException();

		_prevayler.execute(new Consumption(_consumerGetterMethodName, args[0]));
		
		return null;
	}

}
