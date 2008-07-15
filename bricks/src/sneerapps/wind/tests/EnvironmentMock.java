package sneerapps.wind.tests;

import sneerapps.wind.Environment;
import sneerapps.wind.Shout;
import wheel.reactive.sets.SetRegister;
import wheel.reactive.sets.SetSignal;
import wheel.reactive.sets.impl.SetRegisterImpl;
import static wheel.lang.Types.cast;

public class EnvironmentMock implements Environment {

	private final SetRegister<Shout> _tuples = new SetRegisterImpl<Shout>();

	@Override
	public void publish(Object tuple) {
		_tuples.add((Shout) tuple);
	}

	@Override
	public <T> SetSignal<T> subscribe(Class<T> tupleType) {
		return cast(_tuples.output());
	}

}
