package functional.freedom2;

import org.junit.Test;

import wheel.reactive.Signal;
import functional.SovereignFunctionalTest;
import functional.SovereignParty;
import functional.TestDashboard;

public abstract class Freedom2Test extends SovereignFunctionalTest {

	
	@Test (timeout = 1000)
	public void testRemoteNameChange() {

		waitForValue("Ana Almeida", _b.navigateAndGetName("Ana Almeida"));

		waitForValue("Bruno Barros", _a.navigateAndGetName("Bruno Barros"));

		_b.setOwnName("B. Barros");
		waitForValue("B. Barros", _a.navigateAndGetName("Bruno Barros"));

		_b.setOwnName("Dr Barros");
		waitForValue("Dr Barros", _a.navigateAndGetName("Bruno Barros"));

	}

	
	@Test //(timeout = 3000)
	public void testNicknames() {
		if (!TestDashboard.newTestsShouldRun()) return;
		
		waitForValue("Bruno Barros", _a.navigateAndGetName("Bruno Barros"));

		SovereignParty c = _community.createParty("Carla Costa");
		SovereignParty d = _community.createParty("Denis Dalton");
		
		_a.bidirectionalConnectTo(c);
		_b.bidirectionalConnectTo(c);
		c.bidirectionalConnectTo(d);

		_a.giveNicknameTo(_b, "Bruno");
		_b.giveNicknameTo(_a, "Aninha");
		_a.giveNicknameTo(c, "Carla");
		c.giveNicknameTo(_a, "Ana");
		c.giveNicknameTo(d, "Dedé");
		
		waitForValue("Bruno Barros", _a.navigateAndGetName("Bruno"));
		waitForValue("Ana Almeida", _b.navigateAndGetName("Carla Costa/Ana"));
		waitForValue("Ana Almeida", _a.navigateAndGetName("Bruno/Aninha"));
		waitForValue("Bruno Barros", _a.navigateAndGetName("Bruno/Aninha/Bruno"));
		waitForValue("Denis Dalton", _a.navigateAndGetName("Bruno/Carla Costa/Dedé"));
		
		_a.giveNicknameTo(_b, "Bruninho");
		waitForValue("Bruno Barros", _a.navigateAndGetName("Bruninho"));
		
		throw new RuntimeException("Uncomment the timeout at the top of this test and the lines above.");
	}

	private void waitForValue(Object expectedValue, Signal<? extends Object> signal) {
		String previousMessage = null;

		while (true) {
			if (expectedValue == null && signal.currentValue() == null) return;
			if (expectedValue != null && expectedValue.equals(signal.currentValue())) return;

			String message = "Expected: " + expectedValue + " Found: " + signal.currentValue();
			if (!message.equals(previousMessage)) {
				previousMessage = message;
				//System.out.println(message);
			}
			
			Thread.yield();
		}
	}

}
