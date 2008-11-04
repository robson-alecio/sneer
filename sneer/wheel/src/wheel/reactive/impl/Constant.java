package wheel.reactive.impl;

public class Constant<TYPE> extends AbstractSignal<TYPE> {

	private final TYPE _constantValue;
	
	public Constant(TYPE constantValue){
		_constantValue = constantValue;
	}
	
	@Override
	public TYPE currentValue() {
		return _constantValue;
	}
}