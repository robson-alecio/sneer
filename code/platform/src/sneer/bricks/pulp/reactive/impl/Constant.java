package sneer.bricks.pulp.reactive.impl;


class ConstantImpl<TYPE> extends AbstractSignal<TYPE> {

	private final TYPE _constantValue;
	
	public ConstantImpl(TYPE constantValue){
		_constantValue = constantValue;
	}
	
	@Override
	public TYPE currentValue() {
		return _constantValue;
	}

}