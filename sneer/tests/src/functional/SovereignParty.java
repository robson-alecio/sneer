package functional;

import java.io.File;

import snapps.wind.Shout;
import sneer.kernel.container.Brick;
import sneer.pulp.deployer.BrickBundle;
import sneer.pulp.keymanager.PublicKey;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;


public interface SovereignParty {

	String ownName();
	void setOwnName(String newName);
	
	
	PublicKey ownPublicKey();

	
	void bidirectionalConnectTo(SovereignParty peer);
	void giveNicknameTo(SovereignParty peer, String nickname);
	Signal<String> navigateAndGetName(String nicknamePath);
	

	BrickBundle publishBricks(File sourceDirectory);
	Brick produce(Class<? extends Brick> brick);
	
	
	void shout(String string);
	ListSignal<Shout> shoutsHeard();

}
