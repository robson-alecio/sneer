package sneer.pulp.dyndns.ownaccount.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.dyndns.ownaccount.Account;
import sneer.pulp.dyndns.ownaccount.OwnAccountKeeper;

public class OwnAccountKeeperTest extends TestThatIsInjected {

	@Inject
	private static OwnAccountKeeper _subject;
	
	@Test
	public void testAccountKeeper() {
		assertNull(ownAccount());
		
		_subject.accountSetter().consume(new Account("neide.dyndns.org", "neide", "abc123"));
		assertEquals("neide.dyndns.org", ownAccount().host);
		assertEquals("neide", ownAccount().dynDnsUser);
		assertEquals("abc123", ownAccount().password);
	}

	private Account ownAccount() {
		return _subject.ownAccount().currentValue();
	} 
	
}
