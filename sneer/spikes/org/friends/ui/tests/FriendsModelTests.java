package org.friends.ui.tests;

import junit.framework.TestCase;

import org.friends.ui.FriendsModel;
import org.friends.ui.FriendsModelImpl;

import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessImpl;
import sneer.kernel.business.Contact;
import wheel.reactive.tests.LogListReceiver;

public class FriendsModelTests extends TestCase {

	
	private FriendsModel _model;
	private LogListReceiver<Contact> _logListReceiver;
	private Business _business;

	@Override
	protected void setUp() throws Exception {
		_business = new BusinessImpl();
		_model = new FriendsModelImpl(_business);
		_logListReceiver = new LogListReceiver<Contact>();
		_model.friends().addListReceiver(_logListReceiver);
	}
	
	public void testFriendAddition(){
		_model.addFriend("Lola");
		
		String expectedListLog = 
			"List replaced with: [], list is: []\n" +
			"List element added at 0, list is: [Lola - :0]\n";
		
		assertEquals(expectedListLog, _logListReceiver.log());
	}
	
	public void testFriendRemoval(){
		
		_model.addFriend("Lola");
		_model.removeFriend(_model.friends().currentValue().get(0));
		
		String expectedListLog = 
			"List replaced with: [], list is: []\n" +
			"List element added at 0, list is: [Lola - :0]\n" +
			"List element removed at 0, list is: []\n";
		
		assertEquals(expectedListLog, _logListReceiver.log());
	}
}
