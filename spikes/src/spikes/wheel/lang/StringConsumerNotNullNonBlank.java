package spikes.wheel.lang;

import sneer.foundation.lang.PickyConsumer;
import sneer.foundation.lang.exceptions.Refusal;

public class StringConsumerNotNullNonBlank implements PickyConsumer<String> {

	private final String _friendlyName;
	private final PickyConsumer<String> _endConsumer;

	public StringConsumerNotNullNonBlank(String friendlyName,	PickyConsumer<String> endConsumer) {
		_friendlyName = friendlyName;
		_endConsumer = endConsumer;
	}

	@Override
	public void consume(String valueObject) throws Refusal {
		if (valueObject == null)
			throw new Refusal(_friendlyName + " cannot be null.");
		
		if (valueObject.equals(""))
			throw new Refusal(_friendlyName + " cannot be blank.");
		
		_endConsumer.consume(valueObject);

	}

}
