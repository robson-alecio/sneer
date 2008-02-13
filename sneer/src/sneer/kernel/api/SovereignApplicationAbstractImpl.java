package sneer.kernel.api;

import java.util.Collections;
import java.util.List;

import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.DropAction;
import wheel.io.ui.Action;
import wheel.lang.Casts;

public abstract class SovereignApplicationAbstractImpl implements SovereignApplication{

	protected SovereignApplicationNeeds _config;

	public List<Action> mainActions() {
		return Casts.uncheckedGenericCast(Collections.EMPTY_LIST);
	}
	
	public List<ContactAction> contactActions() {
		return Casts.uncheckedGenericCast(Collections.EMPTY_LIST);
	}

	public List<DropAction> dropActions() {
		return Casts.uncheckedGenericCast(Collections.EMPTY_LIST);
	}

	public void init(SovereignApplicationNeeds config) {
		_config = config;
	}

	public Object initialState() { //Refactor: Delete this.
		return null;
	}

}
