package functionaltests;


public interface SovereignParty {

	String ownName();
	void setOwnName(String newName);
	
    /**
     * Create a bidirectional connection between two peers
     */
	void connectTo(SovereignParty peer);
	
	void giveNicknameTo(SovereignParty peer, String nickname);
	SovereignParty navigateTo(String... nicknamePath);



}
