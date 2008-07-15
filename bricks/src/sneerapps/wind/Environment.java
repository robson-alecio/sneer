package sneerapps.wind;

import wheel.reactive.sets.SetSignal;

public interface Environment {

	void publish(Object tuple);

	<T> SetSignal<T> subscribe(Class<T> tupleType);

}
