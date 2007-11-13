package addressbook;

import java.util.List;

import sneer.kernel.api.SovereignApplicationAbstractImpl;
import wheel.io.ui.Action;
import addressbook.business.businessImpl.AddressBookSourceImpl;

public class Application extends SovereignApplicationAbstractImpl{

	private AddressBookApp _delegate;

	@Override
	public String defaultName() {
		return "Address Book";
	}

	@Override
	public int trafficPriority() {
		return 2;
	}

	@Override
	public void start() {
		_delegate = new AddressBookApp(_config);
	}
	
	@Override
	public List<Action> mainActions() {
		return _delegate.mainActions();
	}

	@Override
	public Object initialState() {
		return new AddressBookSourceImpl();
	}
	

}
