package snapps.wind.impl.bubble;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.prevayler.TransactionWithQuery;

import wheel.lang.Consumer;
import wheel.lang.FrozenTime;
import wheel.lang.exceptions.IllegalParameter;

class Consumption implements TransactionWithQuery {

	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	Consumption(List<String> pathToConsumer, Object valueObject) {
		_consumerGetterPath = pathToConsumer.toArray(EMPTY_STRING_ARRAY);
		_valueObject = valueObject;
	}

	private final String[] _consumerGetterPath;
	private final Object _valueObject;

	public Object executeAndQuery(Object stateMachine, Date date) throws IllegalParameter {
		FrozenTime.freezeForCurrentThread(date.getTime());
		Consumer<Object> consumer = navigateToConsumer(stateMachine);
		consumer.consume(_valueObject);
		return null;
	}

	private Consumer<Object> navigateToConsumer(Object stateMachine) {
		Object candidate = stateMachine;

		for (int i = 0; i < _consumerGetterPath.length; i++)
			candidate = invoke(candidate, _consumerGetterPath[i]);
		
		return (Consumer<Object>)candidate;
	}


	private Object invoke(Object candidate, String getterName) {
		try {
			Method getter = candidate.getClass().getMethod(getterName, new Class[0]);
			return getter.invoke(candidate, new Object[0]);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}


	private static final long serialVersionUID = 1L;

}