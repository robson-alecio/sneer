package sneer.kernel.gui.contacts.tests;

import junit.framework.TestCase;
import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessFactory;
import sneer.kernel.business.Contact;
import sneer.kernel.gui.contacts.FriendsModel;
import sneer.kernel.gui.contacts.FriendsModelImpl;
import wheel.reactive.tests.LogListReceiver;

public class FriendsModelTests extends TestCase {

	
	private FriendsModel _model;
	private LogListReceiver<Contact> _logListReceiver;
	private Business _business;

	@Override
	protected void setUp() throws Exception {
		_business = new BusinessFactory().createBusiness();
		_model = new FriendsModelImpl(_business);
		_logListReceiver = new LogListReceiver<Contact>();
		_model.friends().addListReceiver(_logListReceiver);
	}
	
	public void testFriendAddition(){
		_model.addFriend("Lola");
		
		String expectedListLog = 
			"List replaced with: [], list is: []\n" +
			"List element added at 0, list is: [Off :(  Lola - :0]\n";
		
		assertEquals(expectedListLog, _logListReceiver.log());
	}
	
	public void testFriendRemoval(){
		
		_model.addFriend("Lola");
		_model.removeFriend(_model.friends().currentValue().get(0));
		
		String expectedListLog = 
			"List replaced with: [], list is: []\n" +
			"List element added at 0, list is: [Off :(  Lola - :0]\n" +
			"List element removed at 0, list is: []\n";
		
		assertEquals(expectedListLog, _logListReceiver.log());
	}
}
