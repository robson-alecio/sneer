package wheel.lang;

import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.SourceImpl;

public class StringConsumerNotNullNonBlank implements Consumer<String> {

	private final Consumer<String> _endConsumer;
	private final String _friendlyName;

	public StringConsumerNotNullNonBlank(Consumer<String> endConsumer,
			String friendlyName) {
				_endConsumer = endConsumer;
				_friendlyName = friendlyName;
		
	}

	@Override
	public void consume(String valueObject) throws IllegalParameter {
		if (valueObject == null)
			throw new IllegalParameter("" + _friendlyName + " must be not null.");
		
		if (valueObject.equals(""))
			throw new IllegalParameter("" + _friendlyName + " must be not blank.");
		
		_endConsumer.consume(valueObject);

	}

}
