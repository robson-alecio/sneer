package sneer.remote;

public class SetSmokeSignal extends AbstractSmokeSignal {

	private final Object _elementAdded;
	private final Object _elementRemoved;

	protected SetSmokeSignal(int indianId, Object elementAdded, Object elementRemoved) {
		super(indianId);
		_elementAdded = elementAdded;
		_elementRemoved = elementRemoved;
	}
	
	public Object getElementAdded() {
		return _elementAdded;
	}

	public Object getElementRemoved() {
		return _elementRemoved;
	}

	private static final long serialVersionUID = 1L;
}
