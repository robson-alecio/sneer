package functional;

import org.junit.Before;

public abstract class SovereignFunctionalTest {

	protected abstract SovereignCommunity createNewCommunity();

	protected SovereignCommunity _community;
	
	protected SovereignParty _a;
	protected SovereignParty _b;
	protected SovereignParty _c;
	protected SovereignParty _d;
	
	@Before
	public void initNewCommunity() {
		_community = createNewCommunity();
		createAndConnectParties();
	}
	

	private void createAndConnectParties() {
		
		_a = _community.createParty("Ana Almeida");
		_b = _community.createParty("Bruno Barros");
		_c = _community.createParty("Carla Costa");
		_d = _community.createParty("Denis Dalton");
		
//		_a.bidirectionalConnectTo(_b);
//		_a.bidirectionalConnectTo(_c);
//		_b.bidirectionalConnectTo(_c);
//		_c.bidirectionalConnectTo(_d);
	
	}

}
