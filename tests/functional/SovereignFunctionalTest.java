package functional;

import org.junit.After;
import org.junit.Before;

public abstract class SovereignFunctionalTest {

	protected abstract SovereignCommunity createNewCommunity();

	protected SovereignCommunity _community;
	
	protected SovereignParty _a;
	protected SovereignParty _b;
	
	@Before
	public void initNewCommunity() {
		_community = createNewCommunity();
		createAndConnectParties();
	}
	
	@After
	public void tearDown() {
		_community.clearResources("Ana Almeida");
		_community.clearResources("Bruno Barros");
	}

	private void createAndConnectParties() {
		
		_a = _community.createParty("Ana Almeida");
		_b = _community.createParty("Bruno Barros");
		
		_a.bidirectionalConnectTo(_b);
	
	}

}
