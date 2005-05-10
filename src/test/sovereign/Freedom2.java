//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sovereign;

import java.io.IOException;

import org.prevayler.foundation.network.NetworkMock;
import org.prevayler.foundation.network.OldNetwork;

import sovereign.remote.RemoteLife;
import sovereign.remote.LifeServer;


public class Freedom2 extends Freedom1 {
	
	protected Life _ziba;
	protected Life _fefe;
	protected Life _roberts;

	protected OldNetwork _ipNetwork;

	
	public void setUp() throws Exception {
		super.setUp();
		_ipNetwork = new NetworkMock();
		new LifeServer(_me, _ipNetwork.openObjectServerSocket(7000));

		helpFriendsAchieveSovereignty();
		
		LifeView.CALLING_CONTACT.set(null);
	}

    public void testNicknames() throws Exception {
    	changeAFewNicknames();
    	forgetAFewNicknames();
    	tryToDuplicateNicknames();
    }
	
    public void testContactsChangingTheirNames() throws Exception {
    	_ziba.changeName("humberto_francisco_soares");
    	checkName("humberto_francisco_soares", myContact("Ziba"));
    
    	_fefe.changeName("luiz fernando dos s sabino");
    	checkName("luiz fernando dos s sabino", myContact("Fefe"));
    
    	_ziba.changeName("humberto f soares");
    	checkName("humberto f soares", myContact("Ziba"));
    }

    public void testContactNavigation() throws Exception {
    	assertTrue(myContact("Ziba").nicknames().contains("Roberts"));
    	checkName("Roberto Jover Lázaro", myContact("Ziba").contact("Roberts"));
    	
    	checkName("Luiz Fernando dos Santos Sabino", myContact("Ziba").contact("Roberts").contact("Fefe"));
    	
    	assertNull("Contact 'Zabumba' does not exist!", myContact("Ziba").contact("Zabumba"));
    }
    
    
	protected void helpFriendsAchieveSovereignty() throws Exception {
		_ziba = newSovereignFriend("Humberto Francisco Soares", 7001);
		_fefe = newSovereignFriend("Luiz Fernando dos Santos Sabino", 7002);
		_roberts = newSovereignFriend("Roberto Jover Lázaro", 7003);

		_me.giveSomebodyANickname(lifeClient(7001), "Ziba");
		_me.giveSomebodyANickname(lifeClient(7002), "Fefe");
		_me.giveSomebodyANickname(lifeClient(7003), "Roberts");

		_ziba.giveSomebodyANickname(lifeClient(7000), "Zezo");
    	_fefe.giveSomebodyANickname(lifeClient(7000), "Klaus");
    	_roberts.giveSomebodyANickname(lifeClient(7000), "Klaus");

		_ziba.giveSomebodyANickname(lifeClient(7003), "Roberts");
    	_roberts.giveSomebodyANickname(lifeClient(7001), "Humberto");

    	_roberts.giveSomebodyANickname(lifeClient(7002), "Fefe");
		_fefe.giveSomebodyANickname(lifeClient(7003), "Doctor");
	}

	private Life newSovereignFriend(String name, int port) throws Exception {
        Life result = new LifeImpl(name);
		new LifeServer(result, _ipNetwork.openObjectServerSocket(port));
		return result;
    }

	private LifeView lifeClient(int port) throws IOException {
        return RemoteLife.createWith("TODO: find the correct nickname", _ipNetwork.openSocket("localhost", port));
	}
	
	protected LifeView myContact(String nickname) {
		return _me.contact(nickname);
	}

	protected void checkNicknameExists(String nickname) {
		assertTrue(_me.nicknames().contains(nickname));
	}

	protected void checkNickname(String nickname, String name) {
		assertEquals(name, myContact(nickname).name());
	}


    private void changeAFewNicknames() {
    	_me.changeNickname("Roberts", "Doctor");
    	_me.changeNickname("Ziba", "Humba");
    	checkNickname("Humba", "Humberto Francisco Soares");
    	checkNicknameExists("Fefe");
    	checkNicknameExists("Humba");
    	checkNicknameExists("Doctor");
    	assertEquals(3, _me.nicknames().size());
    }

    private void forgetAFewNicknames() throws Exception {
    	_me.giveSomebodyANickname(new LifeImpl("Any Name"), "Bob");
    	_me.giveSomebodyANickname(new LifeImpl("Any Name"), "Mike");
    	_me.forgetNickname("Bob");
    	_me.forgetNickname("Mike");
    	assertEquals(3, _me.nicknames().size());
    }

    private void tryToDuplicateNicknames() throws Exception {
    	try {
    	    _me.giveSomebodyANickname(new LifeImpl("Any Name"), "Fefe");
    		fail("Should not have allowed duplication of Fefe.");
    	} catch(IllegalArgumentException expected) {}
    
    	try {
    		_me.changeNickname("Humba", "Doctor");
    		fail("Should not have allowed duplication of Doctor.");
    	} catch(IllegalArgumentException expected) {}
    }

}
