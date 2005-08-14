package sneer;

import java.io.IOException;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.prevayler.foundation.network.NetworkMock;

import sneer.Sneer.User;

public class SneerTestCase extends MockObjectTestCase {
	
	public void testPersistence() throws IOException {
		
		Mock mocker = mock(User.class);
		User user = (User) mocker.proxy();
		
		mocker.stubs().method("lookAtMe");
		
		mocker.expects(once()).method("confirmName")
			.will(returnValue("Neide da Silva"));
		mocker.expects(once()).method("confirmServerPort")
			.with(eq(Home.DEFAULT_PORT))
			.will(returnValue(4242));
		mocker.expects(once()).method("thoughtOfTheDay")
			.will(returnValue("Las llamas son majores que las ranas!"));
		
		new Sneer(user, new NetworkMock()).editPersonalInfo();		
		
		mocker.expects(once()).method("confirmName")
			.with(eq("Neide da Silva"))
			.will(returnValue("ignored"));
		mocker.expects(once()).method("confirmServerPort")
			.with(eq(4242))
			.will(returnValue(4242));

		Sneer sneer = new Sneer(user, new NetworkMock());
		assertEquals("Las llamas son majores que las ranas!", sneer.life().thoughtOfTheDay()); 
	}

	public void testAddContact() throws IOException {
		
		Mock mocker = mock(User.class);
		User user = (User) mocker.proxy();
		
		mocker.stubs().method("lookAtMe");
		mocker.stubs().method("confirmName")
			.will(returnValue("ignored"));
		mocker.stubs().method("confirmServerPort")
			.will(returnValue(4242));
		
		Sneer sneer = new Sneer(user, new NetworkMock());		
		
		mocker.expects(once()).method("giveNickname")
			.will(returnValue("fefe"));
		mocker.expects(once()).method("informTcpAddress")
			.will(returnValue("localhost:4242"));
		sneer.addContact();

		mocker.expects(once()).method("giveNickname")
			.will(returnValue("bamboo"));
		mocker.expects(once()).method("informTcpAddress")
			.will(returnValue("localhost:4243"));
		sneer.addContact();

		assertTrue(sneer.life().nicknames().contains("fefe"));
		assertTrue(sneer.life().nicknames().contains("bamboo"));

		sneer = new Sneer(user, new NetworkMock());		
		assertTrue(sneer.life().nicknames().contains("fefe"));
		assertTrue(sneer.life().nicknames().contains("bamboo"));
	}
	
	
}
