package sneer.kernel.gui;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.prevayler.Prevayler;

public class ConsumerPersistenceHandler implements InvocationHandler { //Refactor: consider implementing Consumer to be warned when its definition changes.

	private final String _consumerGetter;
	private final Prevayler _prevayler;

	public ConsumerPersistenceHandler(Prevayler prevayler, String consumerGetter) {
		_consumerGetter = consumerGetter;
		_prevayler = prevayler;
	}

	public Object invoke(Object proxyImplied, Method methodImplied, Object[] args)	throws Throwable {
		if(!methodImplied.getName().equals("consume")) throw new UnsupportedOperationException();

		_prevayler.execute(new Consumption(_consumerGetter, args[0]));
		
		return null;
	}

}
