package sneer.pulp.tuples;

import java.lang.reflect.Field;

import org.apache.commons.lang.ArrayUtils;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;

public abstract class Tuple {

	@Inject
	static private Clock _clock;

	@Inject
	static private KeyManager _keys;
	
	
	public Tuple() {
		this(_keys.ownPublicKey(), _clock.time());
	}
	
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
			result = (prime * result) + hashCode(getFieldValue(field, this));
		
		return result;
	}

	private int hashCode(Object obj) {
		if (obj == null) return 0;
		if (obj.getClass().isArray()) return ArrayUtils.hashCode(obj);
		return obj.hashCode();
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
		
		return equals(myValue, hisValue);
	}

	private boolean equals(Object myValue, Object hisValue) {
		if (myValue == null) return hisValue == null;
		if (myValue.getClass().isArray()) return ArrayUtils.isEquals(myValue, hisValue);
		return myValue.equals(hisValue);
	}
	
	
	
}
