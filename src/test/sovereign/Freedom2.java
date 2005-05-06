//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira, Fabio Roger Manera.

package sovereign;

import java.io.IOException;

import junit.textui.TestRunner;

import org.prevayler.foundation.network.NetworkMock;
import org.prevayler.foundation.network.OldNetwork;

import sovereign.remote.LifeResponder;
import sovereign.remote.LifeServer;
import sovereign.remote.RemoteLife;


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
    
	public void testLinks() throws Exception {
		
		Life fabio = newSovereignFriend("Fabio Roger Manera", 7004);
		Life ju = newSovereignFriend("Julinna Saraiva Correia", 7005);
		
		createLink(fabio, "Ju", 7005);

		assertNotNull(ju.contact("Fabio Roger Manera"));
		
		assertEquals(fabio.name(), ju.contact("Fabio Roger Manera").name());
		
		try {
			// FIXME: currently this next call would succeed.. and it shouldnt
			// createLink(ju, "Fabio", 7004);
		} catch (Exception expected) {}
	
		
		ju.changeNickname("Fabio Roger Manera", "Fabio");
		
		ju.changeName("Julinna Correia");
		
		assertEquals(ju.contact("Fabio").contact("Ju").name(), "Julinna Correia");
	}
    
	protected void helpFriendsAchieveSovereignty() throws Exception {
		_ziba = newSovereignFriend("Humberto Francisco Soares", 7001);
		_fefe = newSovereignFriend("Luiz Fernando dos Santos Sabino", 7002);
		_roberts = newSovereignFriend("Roberto Jover Lázaro", 7003);

		createLink(_me, "Ziba", 7001);
		createLink(_me, "Fefe", 7002);
		createLink(_me, "Roberts", 7003);

		_ziba.changeNickname(_me.name(), "Zezo");
		_fefe.changeNickname(_me.name(), "Klaus");
		_roberts.changeNickname(_me.name(), "Klaus");
		
		createLink(_ziba, "Roberts", 7003);
    	_roberts.changeNickname(_ziba.name(), "Humberto");

    	createLink(_roberts, "Fefe", 7002);
		_fefe.changeNickname(_roberts.name(), "Doctor");
	}

	private Life newSovereignFriend(String name, int port) throws Exception {
        Life result = new LifeImpl(name);
		new LifeServer(result, _ipNetwork.openObjectServerSocket(port));
		return result;
    }
	
	public static void main(String[] args) {
		TestRunner.run(Freedom2.class);
	}

	// FIXME: this linking stuff could be done in a better way ... say inside a LifeUtil class.. or .. LifeWorld.. LifeEnvironment.. Playground..
	private void createLink(Life life, String nickname, int port) throws IOException {

		LifeResponder responder = new LifeResponder(life, _ipNetwork.openSocket("localhost", port));
		
		LifeView remoteLife = RemoteLife.createWith(_ipNetwork.openSocket("localhost", port), responder.getServerTicket());
		
		responder.setRemoteLife(remoteLife);
		
		life.giveSomebodyANickname(remoteLife, nickname);
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
