package sneer.bricks.pulp.dyndns.client.tests;

import static sneer.foundation.commons.environments.Environments.my;

import java.io.IOException;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.bricks.hardware.cpu.exceptions.FriendlyException;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.clock.Clock;
import sneer.bricks.pulp.dyndns.client.DynDnsClient;
import sneer.bricks.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.bricks.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import sneer.bricks.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.bricks.pulp.dyndns.updater.BadAuthException;
import sneer.bricks.pulp.dyndns.updater.RedundantUpdateException;
import sneer.bricks.pulp.dyndns.updater.Updater;
import sneer.bricks.pulp.dyndns.updater.UpdaterException;
import sneer.bricks.pulp.propertystore.mocks.TransientPropertyStore;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.threads.mocks.ThreadsMock;
import sneer.foundation.brickness.StoragePath;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.BrickTestRunner;
import sneer.foundation.brickness.testsupport.Contribute;
import sneer.foundation.commons.environments.EnvironmentUtils;

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
	@Contribute final ThreadsMock _threads = new ThreadsMock();
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
		_threads.stepAllSteppers();
		
		startDynDnsClientOnNewEnvironment();
		_threads.stepAllSteppers();
	}

	private void startDynDnsClientOnNewEnvironment() {
		EnvironmentUtils.retrieveFrom(my(BrickTestRunner.class).newTestEnvironment(), 
													  DynDnsClient.class);
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
		_threads.stepAllSteppers();
		
		final Light light = assertBlinkingLight(error);
		
		my(Clock.class).advanceTime(300001);
		
		_threads.stepAllSteppers();
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
		_threads.stepAllSteppers();
		
		final Light light = assertBlinkingLight(error);
		
		// new ip should be ignored while new account is not provided
		_ownIp.setter().consume(newIp);
		
		DynDnsAccount changed = new DynDnsAccount("test.dyndns.org", "test", "*test");
		_ownAccount.setter().consume(changed);

		_threads.stepAllSteppers();
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
		_threads.stepAllSteppers();
		
		assertBlinkingLight(error);
	}

	
	private Light assertBlinkingLight(final Exception expectedError) {
		final ListSignal<Light> lights = my(BlinkingLights.class).lights();
		assertEquals(1, lights.size().currentValue().intValue());
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

