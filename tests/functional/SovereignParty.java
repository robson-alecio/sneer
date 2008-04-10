package functional;

import wheel.reactive.Signal;


public interface SovereignParty {

	String ownName();
	void setOwnName(String newName);
	
	void bidirectionalConnectTo(SovereignParty peer);
	
	void giveNicknameTo(SovereignParty peer, String nickname);
	Signal<String> navigateAndGetName(String nicknamePath);

}
