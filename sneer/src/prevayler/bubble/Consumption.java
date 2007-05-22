package prevayler.bubble;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.prevayler.TransactionWithQuery;

import wheel.lang.Consumer;
import wheel.lang.exceptions.IllegalParameter;

class Consumption implements TransactionWithQuery {

	private final String _consumerGetter;
	private final Object _valueObject;

	Consumption(String consumerGetter, Object valueObject) {
		_consumerGetter = consumerGetter;
		_valueObject = valueObject;
	}


	@SuppressWarnings("unchecked")
	public Object executeAndQuery(Object stateMachine, Date ignored) throws IllegalParameter  {
		Consumer consumer = (Consumer)getConsumer(stateMachine);
		consumer.consume(_valueObject);
		return null;
	}

	private Object getConsumer(Object stateMachine) {
		try {
			Method consumerGetter = stateMachine.getClass().getMethod(_consumerGetter, new Class[0]);
			return consumerGetter.invoke(stateMachine, new Object[0]);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}


	private static final long serialVersionUID = 1L;

}
