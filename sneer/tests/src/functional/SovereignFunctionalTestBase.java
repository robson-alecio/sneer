package functional;

import org.junit.After;
import org.junit.Before;

import sneer.pulp.config.persistence.testsupport.BrickTest;
import wheel.lang.Timebox;

public abstract class SovereignFunctionalTestBase extends BrickTest {

	protected abstract SovereignCommunity createNewCommunity();

	protected SovereignCommunity _community;
	
	protected SovereignParty _a;
	protected SovereignParty _b;
	
	@Before
	public void beforeSovereignTest() {
		new Timebox(8000) { @Override protected void runInTimebox() {
			initCommunity();
		}};
	}

	private void initCommunity() {
		_community = createNewCommunity();
		createAndConnectParties();
	}
	
	@After
	public void releaseCommunity() {
		_community = null;
		_a = null;
		_b = null;
	}
	
	private void createAndConnectParties() {
		
		_a = _community.createParty("Ana Almeida");
		_b = _community.createParty("Bruno Barros");
		
		_a.bidirectionalConnectTo(_b);
	}
}
