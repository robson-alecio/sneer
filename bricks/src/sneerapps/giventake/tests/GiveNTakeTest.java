package sneerapps.giventake.tests;

import org.junit.Assert;
import org.junit.Test;

import sneer.bricks.mesh.Me;
import sneer.bricks.things.Thing;
import sneer.lego.Binder;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.SimpleBinder;
import testdashboard.TestDashboard;
import wheel.reactive.sets.SetSignal;
import functional.SignalUtils;

public class GiveNTakeTest {

	@Test //(timeout = 8000)
	public void testSimpleDeal() {
		if (!TestDashboard.newTestsShouldRun()) return;
		
//		Binder binder = new SimpleBinder();
//		binder.bind(Me.class).to(MeMock.class); //Fix
//		Container containerA = ContainerUtils.newContainer(binder);
//		Container containerB = ContainerUtils.newContainer(binder);

		Binder binderA = new SimpleBinder();
		Binder binderB = new SimpleBinder();

		binderA.bind(Me.class).toInstance(new MeMock());
		binderB.bind(Me.class).toInstance(new MeMock());

		System.out.println("teste");
		
		Container containerA = ContainerUtils.newContainer(binderA);
		Container containerB = ContainerUtils.newContainer(binderB);
		

		
		
		GiveNTakeUser _ana = containerA.produce(GiveNTakeUser.class);
		GiveNTakeUser _bob = containerB.produce(GiveNTakeUser.class);
		
		_ana.connectTo(_bob);
		_bob.connectTo(_ana);

		_ana.advertise("Lovely 3 room Apartment Pinheiros", "Lovely apartment in Pinheiros disctrict, São Paulo. 3 rooms. Kitchen. 150m2");
		SetSignal<Thing> found = _bob.search("apartment \"são paulo\"");
		
		SignalUtils.waitForValue(1, found.size());
		
		Assert.fail("Fix injection above");
		Assert.fail("make connectTo bidirectional");
	}
	
}
