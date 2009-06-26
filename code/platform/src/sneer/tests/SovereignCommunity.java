package sneer.tests;


public interface SovereignCommunity {

    SovereignParty createParty(String name);

	void connect(SovereignParty a, SovereignParty b);

}
