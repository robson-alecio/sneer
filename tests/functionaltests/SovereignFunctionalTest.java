package functionaltests;

import org.junit.Before;

public abstract class SovereignFunctionalTest {

	protected SovereignCommunity _community;
	
	@Before
	public void initNewCommunity() {
		_community = createNewCommunity(); 
	}

	protected abstract SovereignCommunity createNewCommunity();

}
