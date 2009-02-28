package sneer.pulp.dyndns.ownaccount.tests;

import org.junit.Test;

import sneer.brickness.testsupport.TestInBrickness;
import sneer.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import static sneer.brickness.Environments.my;

public class DynDnsAccountKeeperTest extends TestInBrickness {

	private final DynDnsAccountKeeper _subject = my(DynDnsAccountKeeper.class);
	
	@Test
	public void testAccountKeeper() {
		assertNull(ownAccount());
		
		_subject.accountSetter().consume(new DynDnsAccount("neide.dyndns.org", "neide", "abc123"));
		assertEquals("neide.dyndns.org", ownAccount().host);
		assertEquals("neide", ownAccount().dynDnsUser);
		assertEquals("abc123", ownAccount().password);
	}

	private DynDnsAccount ownAccount() {
		return _subject.ownAccount().currentValue();
	} 
	
}
