package functional;

import org.junit.After;
import org.junit.Before;

import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.tests.TestThatIsInjected;

public abstract class SovereignFunctionalTest extends TestThatIsInjected {

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
	public void releaseCommunity() {
		_community = null;
		_a = null;
		_b = null;
		ContainerUtils.stopContainer();
	}
	
	private void createAndConnectParties() {
		
		_a = _community.createParty("Ana Almeida");
		_b = _community.createParty("Bruno Barros");
		
		_a.bidirectionalConnectTo(_b);
	}
}
