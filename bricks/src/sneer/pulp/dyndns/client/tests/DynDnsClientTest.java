package sneer.pulp.dyndns.client.tests;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.dyndns.ownaccount.Account;
import sneer.pulp.dyndns.ownaccount.OwnAccountKeeper;
import sneer.pulp.dyndns.ownaccount.impl.SimpleAccount;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.dyndns.updater.BadAuthException;
import sneer.pulp.dyndns.updater.Updater;
import sneer.pulp.dyndns.updater.UpdaterException;
import sneer.pulp.propertystore.mocks.TransientPropertyStore;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class DynDnsClientTest {
	
	/*

Required Client Behavior

    * Send a unique user agent which includes company name, model number, and software build revision.
    * Check that all input is in valid form before updating.
    * Check that any IP obtained through web-based IP detection is a valid dotted quad numeric IP (eg: 1.2.3.4) before sending it in an update.
    * Only update when the IP address is different from the IP of the last update.

Unacceptable Client Behavior

    * Send requests to or access anything other than /nic/update at the host members.dyndns.org.
    * Reverse engineer web requests to our website to create or delete hostnames.
    * Hardcode the IP address of any of DynDNS servers.
    * Attempt to update after receiving the notfqdn, abuse, nohost, badagent, badauth, badsys return codes or repeated nochg return codes without user intervention.
    * Perform DNS updates to determine whether the client IP needs to be updated.
    * Access our web-based IP detection script (http://checkip.dyndns.com/) more than once every 10 minutes

	 */
	
	final Mockery context = new JUnit4Mockery();
	final Register<String> ownIp = new RegisterImpl<String>("123.45.67.89");
	final RegisterImpl<Account> ownAccount = new RegisterImpl<Account>(new SimpleAccount("test.dyndns.org", "test", "test"));
	final OwnIpDiscoverer ownIpDiscoverer = context.mock(OwnIpDiscoverer.class);
	final OwnAccountKeeper ownAccountKeeper = context.mock(OwnAccountKeeper.class);
	final Updater updater = context.mock(Updater.class);
	final TransientPropertyStore propertyStore = new TransientPropertyStore();
	
	@Test
	public void updateOnIpChange() throws Exception {
		context.checking(new Expectations() {{
			exactly(2).of(ownIpDiscoverer).ownIp();
				will(returnValue(ownIp.output()));
				
			atLeast(1).of(ownAccountKeeper).ownAccount();
				will(returnValue(ownAccount.output()));
				
			final Account account = ownAccount.output().currentValue();
			exactly(1).of(updater).update(account.host(), account.user(), account.password(), ownIp.output().currentValue());
		}});
		

		startDynDnsClient();
		
		startDynDnsClient();
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void userInterventionRequiredAfterFailure() throws UpdaterException, IOException {
		
		context.checking(new Expectations() {{
			exactly(1).of(ownIpDiscoverer).ownIp();
				will(returnValue(ownIp.output()));
			atLeast(1).of(ownAccountKeeper).ownAccount();
				will(returnValue(ownAccount.output()));
			
			exactly(1).of(updater).update(anyString(), anyString(), anyString(), with(ownIp.output().currentValue()));
				will(throwException(new BadAuthException()));
		}

		private String anyString() {
			return with(any(String.class));
		}});
		
		startDynDnsClient();
		
		ownIp.setter().consume("111.111.111.111");
		
		context.assertIsSatisfied();
	}

	private void startDynDnsClient() {
		final Container container = ContainerUtils.newContainer(ownIpDiscoverer, ownAccountKeeper, updater, propertyStore);
		container.produce(DynDnsClient.class);
	}

}
