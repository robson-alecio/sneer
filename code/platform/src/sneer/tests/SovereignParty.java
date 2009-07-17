package sneer.tests;

import java.io.File;
import java.io.IOException;



public interface SovereignParty {

	//Freedom1
	String ownName();
	void setOwnName(String newName);
	byte[] seal();

	//Freedom2
	void giveNicknameTo(SovereignParty peer, String nickname);
	boolean isOnline(String nickname);
	void navigateAndWaitForName(String nicknamePath, String expectedName);

	//Freedom5
	void shout(String string);
	void waitForShouts(String shoutsExpected);

	//Freedom7
	void publishBricks(File sourceDirectory) throws IOException;
	void waitForAvailableBrick(String brickName);
	
}
