package sneer.pulp.dyndns.ownaccount.tests;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.dyndns.ownaccount.OwnAccountKeeper;

public class OwnAccountKeeperTest extends TestThatIsInjected {

	@Inject
	private static OwnAccountKeeper _subject;
	
	@Test
	public void testAccountKeeper() {
		assertNull(_subject.ownAccount().currentValue());
		
		//_subject.accountSetter().consume(new Account());
	} 
	
}
