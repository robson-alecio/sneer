package scribble;

import java.util.Collections;
import java.util.List;

import sneer.kernel.api.SovereignApplicationNeeds;
import sneer.kernel.api.SovereignApplication;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.DropAction;
import wheel.io.ui.Action;
import wheel.lang.Casts;

public class Application implements SovereignApplication{

	private Scribble _delegate;

	@Override
	public String defaultName() {
		return "Scribble";
	}

	@Override
	public int trafficPriority() {
		return 2;
	}

	@Override
	public void start(SovereignApplicationNeeds config) {
		_delegate = new Scribble(config);
	}

	@Override
	public List<ContactAction> contactActions() {
		return _delegate.contactActions();
	}
	
	@Override
	public List<DropAction> dropActions() {
		return Casts.uncheckedGenericCast(Collections.EMPTY_LIST);
	}

	@Override
	public List<Action> mainActions() {
		return Casts.uncheckedGenericCast(Collections.EMPTY_LIST);
	}
	
	@Override
	public Object initialState() {
		return null;
	}
	

}
