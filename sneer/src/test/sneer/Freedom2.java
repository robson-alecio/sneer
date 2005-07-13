//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer;

import java.io.IOException;
import java.util.Date;

import org.prevayler.foundation.network.NetworkMock;
import org.prevayler.foundation.network.OldNetwork;

import sneer.life.Life;
import sneer.life.LifeImpl;
import sneer.life.LifeView;
import sneer.remote.Connection;
import sneer.remote.ParallelServer;


public class Freedom2 extends Freedom1 {
	
	protected Life _ziba;
	protected Life _fefe;
	protected Life _roberts;

	protected OldNetwork _ipNetwork;

	
	public void setUp() throws Exception {
		super.setUp();
		//_ipNetwork = new OldNetworkImpl();
		_ipNetwork = new NetworkMock();
		new ParallelServer(_me, _ipNetwork.openObjectServerSocket(7000));

		helpFriendsAchieveSovereignty();
		
		LifeView.CALLING_NICKNAME.set(null);
	}

    public void testNicknames() throws Exception {
    	changeAFewNicknames();
    	forgetAFewNicknames();
    	tryToDuplicateNicknames();
    }
	
    public void testContactsChangingTheirNames() throws Exception {
    	_ziba.name("humberto_francisco_soares");
    	checkName("humberto_francisco_soares", myContact("Ziba"));
    
    	_fefe.name("luiz fernando dos s sabino");
    	checkName("luiz fernando dos s sabino", myContact("Fefe"));
    
    	_ziba.name("humberto f soares");
    	checkName("humberto f soares", myContact("Ziba"));
    }

    public void testContactNavigation() throws Exception {
    	assertTrue(myContact("Ziba").nicknames().contains("Roberts"));
    	checkName("Roberto Jover Lázaro", myContact("Ziba").contact("Roberts"));
    	
    	checkName("Luiz Fernando dos Santos Sabino", myContact("Ziba").contact("Roberts").contact("Fefe"));
    	
    	assertNull("Contact 'Zabumba' should not exist!", myContact("Ziba").contact("Zabumba"));
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
		new ParallelServer(result, _ipNetwork.openObjectServerSocket(port));
		return result;
    }

	private LifeView lifeClient(int port) throws IOException {
        return new Connection(_ipNetwork, "localhost", port).lifeView();
	}
	
	protected LifeView myContact(String nickname) {
		LifeView result = _me.contact(nickname);
		waitForUpdate(result);
		return result;
	}

	protected void waitForUpdate(LifeView result) {
		Date previousSightingDate = result.lastSightingDate();
		while (result.lastSightingDate() == null) Thread.yield();
		while (result.lastSightingDate().equals(previousSightingDate)) Thread.yield();
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

	@Override
	protected void checkName(String expected, LifeView lifeView) {
		waitForUpdate(lifeView);
		super.checkName(expected, lifeView);
	}

}
