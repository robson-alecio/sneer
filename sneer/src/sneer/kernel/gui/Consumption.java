package sneer.kernel.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.prevayler.Transaction;

import wheel.lang.Consumer;
import wheel.lang.exceptions.IllegalParameter;

public class Consumption implements Transaction {

	private final String _consumerGetter;
	private final Object _valueObject;

	public Consumption(String consumerGetter, Object valueObject) {
		_consumerGetter = consumerGetter;
		_valueObject = valueObject;
	}

	@SuppressWarnings("unchecked")
	public void executeOn(Object business, Date ignored) {
		try {
			Method consumerGetter = business.getClass().getMethod(_consumerGetter, new Class[0]);
			Consumer consumer = (Consumer)consumerGetter.invoke(business, new Object[0]);
			consumer.consume(_valueObject);
		} catch (Exception e) {
			throw new RuntimeException(e);
			//Fix: handle exceptions.
		}
	}

	private static final long serialVersionUID = 1L;
}
