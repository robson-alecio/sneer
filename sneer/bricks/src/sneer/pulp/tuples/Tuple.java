package sneer.pulp.tuples;

import java.lang.reflect.Field;

import sneer.pulp.keymanager.PublicKey;

public abstract class Tuple {

	public Tuple(PublicKey pPublisher, long pPublicationTime) {
		publisher = pPublisher;
		publicationTime = pPublicationTime;
	}
	
	public final PublicKey publisher;
	public final long publicationTime;
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		
		for (Field field : getClass().getFields())
			result = (prime * result) + getFieldValue(field, this).hashCode();
		
		return result;
	}

	@Override
	public final boolean equals(Object other) {
		if (this == other) return true;
		if (other == null) return false;
		if (getClass() != other.getClass()) return false;

		for (Field field : getClass().getFields())
			if (!isSameFieldValue(field, other)) return false;
		
		return true;
	}

	private Object getFieldValue(Field field, Object object) {
		try {
			return field.get(object);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("All fields in a Tuple should be public.", e);
		}
	}

	private boolean isSameFieldValue(Field field, Object other) {
		Object myValue = getFieldValue(field, this);
		Object hisValue = getFieldValue(field, other);
		
		return myValue == null
			? hisValue == null
			: myValue.equals(hisValue);
	}
	
	
	
}
