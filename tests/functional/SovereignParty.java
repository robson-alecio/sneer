package functional;


public interface SovereignParty {

	String ownName();
	void setOwnName(String newName);
	
	void bidirectionalConnectTo(SovereignParty peer);
	
	void giveNicknameTo(SovereignParty peer, String nickname);
	String navigateAndGetName(String nicknamePath);

}
