package sneer.kernel.pointofview.tests;


public interface SovereignNetworkSimulator {

	PartySimulator createPartySimulator(String name);

	void connect(PartySimulator a, PartySimulator b);

}
