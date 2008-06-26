package functional;

import java.io.File;

import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import sneer.bricks.keymanager.PublicKey;
import wheel.reactive.Signal;


public interface SovereignParty {

	String ownName();
	void setOwnName(String newName);
	
	
	PublicKey ownPublicKey();

	
	void bidirectionalConnectTo(SovereignParty peer);
	void giveNicknameTo(SovereignParty peer, String nickname);
	Signal<String> navigateAndGetName(String nicknamePath);
	

	BrickBundle publishBrick(File sourceDirectory);
	void meToo(SovereignParty party, String brickName) throws Exception;
	BrickFile brick(String brickName);
	Object produce(String brickName);

}
