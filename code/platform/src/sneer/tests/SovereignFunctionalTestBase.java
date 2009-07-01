package sneer.tests;

import org.junit.After;

import sneer.foundation.brickness.testsupport.BrickTest;

public abstract class SovereignFunctionalTestBase extends BrickTest {

	private SovereignCommunity _community = createNewCommunity();
	
	private SovereignParty _a;
	private SovereignParty _b;

	
	protected abstract SovereignCommunity createNewCommunity();
	
	
	protected SovereignParty a() {
		init();
		return _a;
	}

	protected SovereignParty b() {
		init();
		return _b;
	}

	protected SovereignParty createParty(String name) {
		return _community.createParty(name);
	}

	private void init() { //This is done lazily because it has to run as part of the test and not during the constructor or even during the @Before method because JUnit will not count those as part of the test's timeout. :(
		if (_a != null) return;
		
		_a = _community.createParty("Ana Almeida");
		_b = _community.createParty("Bruno Barros");
		
		connect(_a, _b);
	}

	protected void connect(SovereignParty a, SovereignParty b) {
		_community.connect(a, b);
	}

	
	@After
	public void releaseCommunity() {
		_community = null;
		_a = null;
		_b = null;
	}
	
	
}
