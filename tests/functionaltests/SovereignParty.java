package functionaltests;

import sneer.network.SovereignPeer;


public interface SovereignParty extends SovereignPeer {

	String ownName();
	void setOwnName(String newName);
	
	void bidirectionalConnectTo(SovereignParty peer);
	
	void giveNicknameTo(SovereignParty peer, String nickname);
	SovereignParty navigateTo(String... nicknamePath);

}
