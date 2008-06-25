package functional.freedom2;

import org.junit.Test;

import wheel.reactive.Signal;
import functional.SignalUtils;
import functional.SovereignFunctionalTest;
import functional.SovereignParty;

public abstract class Freedom2Test extends SovereignFunctionalTest {

	
	@Test (timeout = 1000)
	public void testRemoteNameChange() {

		SignalUtils.waitForValue("Ana Almeida", _b.navigateAndGetName("Ana Almeida"));

		SignalUtils.waitForValue("Bruno Barros", _a.navigateAndGetName("Bruno Barros"));

		_b.setOwnName("B. Barros");
		SignalUtils.waitForValue("B. Barros", _a.navigateAndGetName("Bruno Barros"));

		_b.setOwnName("Dr Barros");
		SignalUtils.waitForValue("Dr Barros", _a.navigateAndGetName("Bruno Barros"));
	}

	
	@Test (timeout = 2000)
	public void testNicknames() {
		SovereignParty c = _community.createParty("Carla Costa");
		SovereignParty d = _community.createParty("Denis Dalton");
		
		_a.bidirectionalConnectTo(c);
		_b.bidirectionalConnectTo(c);
		c.bidirectionalConnectTo(d);

		SignalUtils.waitForValue("Carla Costa", _b.navigateAndGetName("Carla Costa"));

		_a.giveNicknameTo(_b, "Bruno");
		_b.giveNicknameTo(_a, "Aninha");
		_a.giveNicknameTo(c, "Carla");
		c.giveNicknameTo(d, "Dedé");
		
		SignalUtils.waitForValue("Bruno Barros", _a.navigateAndGetName("Bruno"));
		SignalUtils.waitForValue("Ana Almeida", _b.navigateAndGetName("Carla Costa/Ana Almeida"));
		SignalUtils.waitForValue("Ana Almeida", _a.navigateAndGetName("Bruno/Aninha"));
		SignalUtils.waitForValue("Bruno Barros", _a.navigateAndGetName("Bruno/Aninha/Bruno"));
		SignalUtils.waitForValue("Denis Dalton", _a.navigateAndGetName("Bruno/Carla Costa/Dedé"));

		Signal<String> anasName = _b.navigateAndGetName("Carla Costa/Ana Almeida");
		_b.giveNicknameTo(c, "Carlinha");
		c.giveNicknameTo(_a, "Aninha");
		SignalUtils.waitForValue("Ana Almeida", _b.navigateAndGetName("Carlinha/Aninha"));
		
		_a.setOwnName("Dr Ana");
		SignalUtils.waitForValue("Dr Ana", anasName);
	}


}
