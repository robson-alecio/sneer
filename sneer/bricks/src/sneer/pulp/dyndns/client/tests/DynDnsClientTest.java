package sneer.pulp.dyndns.client.tests;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.brickness.StoragePath;
import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.BrickTestRunner;
import sneer.brickness.testsupport.Contribute;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.clock.Clock;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.dyndns.updater.BadAuthException;
import sneer.pulp.dyndns.updater.RedundantUpdateException;
import sneer.pulp.dyndns.updater.Updater;
import sneer.pulp.dyndns.updater.UpdaterException;
import sneer.pulp.propertystore.mocks.TransientPropertyStore;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.threadpool.mocks.ThreadPoolMock;
import sneer.software.exceptions.FriendlyException;

public class DynDnsClientTest extends BrickTest {
	
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
	
	final Register<String> _ownIp = my(Signals.class).newRegister("123.45.67.89");
	final DynDnsAccount _account = new DynDnsAccount("test.dyndns.org", "test", "test");
	final Register<DynDnsAccount> _ownAccount = my(Signals.class).newRegister(_account);
	
	@Contribute final OwnIpDiscoverer _ownIpDiscoverer = mock(OwnIpDiscoverer.class);
	@Contribute final DynDnsAccountKeeper _ownAccountKeeper = mock(DynDnsAccountKeeper.class);
	@Contribute final Updater _updater = mock(Updater.class);
	@Contribute final TransientPropertyStore _propertyStore = new TransientPropertyStore();
	@Contribute final ThreadPoolMock _threadPool = new ThreadPoolMock();
	@Contribute final StoragePath _storagePath = new StoragePath(){@Override public String get() {
		return tmpDirectory().getAbsolutePath();
	}};
	
	@Test
	public void updateOnIpChange() throws Exception {
		checking(new Expectations() {{
			allowing(_ownIpDiscoverer).ownIp();
				will(returnValue(_ownIp.output()));
				
			atLeast(1).of(_ownAccountKeeper).ownAccount();
				will(returnValue(_ownAccount.output()));
				
			final DynDnsAccount account = _ownAccount.output().currentValue();
			exactly(1).of(_updater).update(account.host, account.user, account.password, _ownIp.output().currentValue());
		}});
		
		startDynDnsClientOnNewEnvironment();
		_threadPool.runAllActors();
		
		startDynDnsClientOnNewEnvironment();
		_threadPool.runAllActors();
	}

	private void startDynDnsClientOnNewEnvironment() {
		Environments.runWith(my(BrickTestRunner.class).newTestEnvironment(), new Runnable() { @Override public void run() {
			my(DynDnsClient.class);
		}});
	}
	
	@Test
	public void retryAfterIOException() throws Exception {
		
		final IOException error = new IOException();
		
		checking(new Expectations() {{
			allowing(_ownIpDiscoverer).ownIp();
				will(returnValue(_ownIp.output()));
				
			allowing(_ownAccountKeeper).ownAccount();
				will(returnValue(_ownAccount.output()));
				
			final DynDnsAccount account = _ownAccount.output().currentValue();
			exactly(1).of(_updater).update(account.host, account.user, account.password, _ownIp.output().currentValue());
				will(throwException(error));
				
			exactly(1).of(_updater).update(account.host, account.user, account.password, _ownIp.output().currentValue());
		}});
		

		startDynDnsClient();
		_threadPool.runAllActors();
		
		final Light light = assertBlinkingLight(error, my(Environment.class));
		
		my(Clock.class).advanceTime(300001);
		
		_threadPool.runAllActors();
		assertFalse(light.isOn());
	}
	
	@Test
	public void userInterventionRequiredAfterFailure() throws UpdaterException, IOException {
		
		final BadAuthException error = new BadAuthException();
		final DynDnsAccount account = _ownAccount.output().currentValue();
		final String newIp = "111.111.111.111";
		
		checking(new Expectations() {{
			allowing(_ownIpDiscoverer).ownIp();
				will(returnValue(_ownIp.output()));
			allowing(_ownAccountKeeper).ownAccount();
				will(returnValue(_ownAccount.output()));
			
			exactly(1).of(_updater).update(account.host, account.user, account.password, _ownIp.output().currentValue());
				will(throwException(error));
				
			exactly(1).of(_updater).update(account.host, account.user, "*" + account.password, newIp);
		}});
		
		startDynDnsClient();
		_threadPool.runAllActors();
		
		final Light light = assertBlinkingLight(error, my(Environment.class));
		
		// new ip should be ignored while new account is not provided
		_ownIp.setter().consume(newIp);
		
		DynDnsAccount changed = new DynDnsAccount("test.dyndns.org", "test", "*test");
		_ownAccount.setter().consume(changed);

		_threadPool.runAllActors();
		assertFalse(light.isOn());
		
	}

	@Test
	public void redundantUpdate() throws UpdaterException, IOException {
		
		final RedundantUpdateException error = new RedundantUpdateException();
		final DynDnsAccount account = _ownAccount.output().currentValue();
		
		checking(new Expectations() {{
			allowing(_ownIpDiscoverer).ownIp();	will(returnValue(_ownIp.output()));
			allowing(_ownAccountKeeper).ownAccount(); will(returnValue(_ownAccount.output()));
			
			exactly(1).of(_updater).update(account.host, account.user, account.password, _ownIp.output().currentValue());
				will(throwException(error));
		}});
		
		startDynDnsClient();
		_threadPool.runAllActors();
		
		assertBlinkingLight(error, my(Environment.class));
	}

	
	private Light assertBlinkingLight(final Exception expectedError, final Environment container) {
		final ListSignal<Light> lights = container.provide(BlinkingLights.class).lights();
		assertEquals(1, lights.currentSize());
		final Light light = lights.currentGet(0);
		assertTrue(light.isOn());
		if (expectedError instanceof FriendlyException) {
			assertEquals(((FriendlyException)expectedError).getHelp(), light.helpMessage());
		}
		assertSame(expectedError, light.error());
		return light;
	}

	private void startDynDnsClient() {
		my(DynDnsClient.class);
	}
}

