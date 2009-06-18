package sneer.bricks.pulp.tuples.impl;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.prevayler.TransactionWithQuery;

import sneer.foundation.lang.PickyConsumer;
import sneer.foundation.lang.exceptions.Refusal;

class Consumption implements TransactionWithQuery {

	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	Consumption(List<String> pathToConsumer, Object valueObject) {
		_consumerGetterPath = pathToConsumer.toArray(EMPTY_STRING_ARRAY);
		_valueObject = valueObject;
	}

	private final String[] _consumerGetterPath;
	private final Object _valueObject;

	public Object executeAndQuery(Object stateMachine, Date date) throws Refusal {
		PickyConsumer<Object> consumer = navigateToConsumer(stateMachine);
		consumer.consume(_valueObject);
		return null;
	}

	private PickyConsumer<Object> navigateToConsumer(Object stateMachine) {
		Object candidate = stateMachine;

		for (int i = 0; i < _consumerGetterPath.length; i++)
			candidate = invoke(candidate, _consumerGetterPath[i]);
		
		return (PickyConsumer<Object>)candidate;
	}


	private Object invoke(Object candidate, String getterName) {
		Method getter = null;
		try {
			getter = candidate.getClass().getMethod(getterName, new Class[0]);
			getter.setAccessible(true);
			return getter.invoke(candidate, new Object[0]);
		} catch (Exception e) {
			throw new IllegalStateException("Exception trying to invoke " + candidate.getClass() + "." + getterName, e);
		}
	}


	private static final long serialVersionUID = 1L;

}
