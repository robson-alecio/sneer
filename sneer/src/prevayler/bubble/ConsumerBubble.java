package prevayler.bubble;

import org.prevayler.Prevayler;

import wheel.lang.Consumer;
import wheel.lang.exceptions.IllegalParameter;


@SuppressWarnings("unchecked")
class ConsumerBubble implements Consumer {

	private final String _consumerGetterMethodName;
	private final Prevayler _prevayler;

	ConsumerBubble(Prevayler prevayler, String consumerGetterMethodName) {
		_consumerGetterMethodName = consumerGetterMethodName;
		_prevayler = prevayler;
	}

//	public Object invoke(Object proxyImplied, Method methodImplied, Object[] args)	throws Throwable {
//		if(!methodImplied.getName().equals("consume")) throw new UnsupportedOperationException();
//
//		_prevayler.execute(new Consumption(_consumerGetterMethodName, args[0]));
//		
//		return null;
//	}

	public void consume(Object vo) throws IllegalParameter {
		try {
			_prevayler.execute(new Consumption(_consumerGetterMethodName, vo));
		} catch (IllegalParameter e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
