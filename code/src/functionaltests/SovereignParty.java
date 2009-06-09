package functionaltests;

import java.io.File;

import snapps.wind.Shout;
import sneer.brickness.PublicKey;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.collections.ListSignal;


public interface SovereignParty {

	String ownName();
	void setOwnName(String newName);
	
	
	PublicKey ownPublicKey();

	
	void bidirectionalConnectTo(SovereignParty peer);
	void giveNicknameTo(SovereignParty peer, String nickname);
	Signal<String> navigateAndGetName(String nicknamePath);
	

	void publishBricks(File sourceDirectory);
	
	void shout(String string);
	ListSignal<Shout> shoutsHeard();

}
