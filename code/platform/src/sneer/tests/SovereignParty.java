package sneer.tests;

import java.io.File;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.snapps.wind.Shout;


public interface SovereignParty {

	String ownName();
	void setOwnName(String newName);
	
	void bidirectionalConnectTo(SovereignParty peer);
	void giveNicknameTo(SovereignParty peer, String nickname);
	Signal<String> navigateAndGetName(String nicknamePath);
	

	void publishBricks(File sourceDirectory);
	
	void shout(String string);
	ListSignal<Shout> shoutsHeard();
	
}
