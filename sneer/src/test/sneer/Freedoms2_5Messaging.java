//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer;

import sneer.life.LifeView;
import sneer.life.NoneOfYourBusiness;


public class Freedoms2_5Messaging extends Freedom2  {

	public void testMessaging() throws Exception {
		try {
		    _ziba.send("Hello my friend", "Fulano");
		    fail("Fulano is not a valid nickname.");
		} catch (IllegalArgumentException expected) {}
		
		_ziba.send("Hello Klaus", "Zezo");
		_ziba.send("Hello Roberts", "Roberts");
		
	    assertTrue(_ziba.messagesSentTo("Zezo").contains("Hello Klaus"));

	    waitForUpdates(_roberts.contact("Humberto"));
	    assertTrue(_roberts.contact("Humberto").messagesSentTo("Roberts").contains("Hello Roberts"));
	}

	public void testPrivateMessaging() throws Exception {
		_ziba.send("Hello Klaus", "Zezo");
		
		LifeView.CALLING_NICKNAME.set("Zezo");
		assertTrue(myContact("Ziba").messagesSentToMe().contains("Hello Klaus"));
		try {
			myContact("Ziba").messagesSentTo("Sweetie");
			fail("The messages Ziba sends to 'Sweetie' are none of your business.");
		} catch (NoneOfYourBusiness expected) {}
	   
	}

}
