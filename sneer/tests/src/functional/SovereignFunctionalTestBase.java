package functional;

import org.junit.After;
import org.junit.Before;

import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.tests.TestThatIsInjected;
import wheel.lang.Timebox;

public abstract class SovereignFunctionalTestBase extends TestThatIsInjected {

	protected abstract SovereignCommunity createNewCommunity();

	protected SovereignCommunity _community;
	
	protected SovereignParty _a;
	protected SovereignParty _b;
	
	@Before
	public void beforeSovereignTest() {
		new Timebox(5000) { @Override public void run() {
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
		ContainerUtils.stopContainer();
	}
	
	private void createAndConnectParties() {
		
		_a = _community.createParty("Ana Almeida");
		_b = _community.createParty("Bruno Barros");
		
		_a.bidirectionalConnectTo(_b);
	}
}
