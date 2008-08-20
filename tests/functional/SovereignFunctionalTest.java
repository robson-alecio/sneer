package functional;

import org.junit.Before;

import wheel.testutil.TmpDirectoryTest;

public abstract class SovereignFunctionalTest extends TmpDirectoryTest {

	protected abstract SovereignCommunity createNewCommunity();

	protected SovereignCommunity _community;
	
	protected SovereignParty _a;
	protected SovereignParty _b;
	
	@Before
	public void initNewCommunity() {
		_community = createNewCommunity();
		createAndConnectParties();
	}
	
	private void createAndConnectParties() {
		
		_a = _community.createParty("Ana Almeida");
		_b = _community.createParty("Bruno Barros");
		
		_a.bidirectionalConnectTo(_b);
	
	}

}
