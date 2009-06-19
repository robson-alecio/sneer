package sneer.bricks.pulp.dyndns.ownaccount.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.bricks.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import sneer.foundation.brickness.testsupport.BrickTest;

public class DynDnsAccountKeeperTest extends BrickTest {

	private final DynDnsAccountKeeper _subject = my(DynDnsAccountKeeper.class);
	
	@Test
	public void testAccountKeeper() {
		assertNull(ownAccount());
		
		_subject.accountSetter().consume(new DynDnsAccount("neide.dyndns.org", "neide", "abc123"));
		assertEquals("neide.dyndns.org", ownAccount().host);
		assertEquals("neide", ownAccount().user);
		assertEquals("abc123", ownAccount().password);
	}

	private DynDnsAccount ownAccount() {
		return _subject.ownAccount().currentValue();
	} 
	
}
