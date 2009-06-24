package sneer.tests;

import java.io.File;



public interface SovereignParty {

	String ownName();
	void setOwnName(String newName);
	
	void bidirectionalConnectTo(SovereignParty peer);
	boolean isOnline(String string);
	void giveNicknameTo(SovereignParty peer, String nickname);
	void navigateAndWaitForName(String nicknamePath, String expectedName);
	

	void publishBricks(File sourceDirectory);
	
	void shout(String string);
	void waitForShouts(String shoutsExpected);
	
}
