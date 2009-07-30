package sneer.tests;


public interface SovereignCommunity {

    SovereignParty createParty(String name);
    SovereignParty newSession(SovereignParty party);

	void connect(SovereignParty a, SovereignParty b);

	void crash();

}
