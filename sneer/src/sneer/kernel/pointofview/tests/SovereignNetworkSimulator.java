package sneer.kernel.pointofview.tests;


interface SovereignNetworkSimulator {

	PartySimulator createPartySimulator(String name);

	void connect(PartySimulator a, PartySimulator b);

}
