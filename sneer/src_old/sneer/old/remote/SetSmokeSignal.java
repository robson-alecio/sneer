package sneer.old.remote;

import wheel.reactive.sets.SetSignal.SetValueChange;

public class SetSmokeSignal extends AbstractSmokeSignal {

	final SetValueChange<?> _change;

	protected SetSmokeSignal(int indianId, SetValueChange<?> change) {
		super(indianId);
		_change = change;
	}

	private static final long serialVersionUID = 1L;
}
