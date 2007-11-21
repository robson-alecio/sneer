package sneer.apps.conversations;


import java.util.List;

import sneer.apps.conversations.business.impl.AppPersistenceSourceImpl;
import sneer.kernel.api.SovereignApplicationAbstractImpl;
import sneer.kernel.gui.contacts.ContactAction;

public class Application extends SovereignApplicationAbstractImpl{

	private ConversationsApp _delegate;
	
	public Application(){
	}

	@Override
	public String defaultName() {
		return "Messages";
	}

	@Override
	public int trafficPriority() {
		return 0;
	}
	
	@Override
	public void start() {
		_delegate = new ConversationsApp(_config);
	}

	@Override
	public List<ContactAction> contactActions() {
		return _delegate.contactActions();
	}
	
	@Override
	public Object initialState() {
		return new AppPersistenceSourceImpl();
	}

}
