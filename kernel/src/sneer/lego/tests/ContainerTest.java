package sneer.lego.tests;

import static org.junit.Assert.assertTrue;
import im.IM;

import java.security.Policy;

import org.junit.Ignore;
import org.junit.Test;

import sneer.lego.Binder;
import sneer.lego.Brick;
import sneer.lego.Container;
import sneer.lego.impl.SimpleBinder;
import sneer.lego.impl.SimpleContainer;
import sneer.lego.tests.impl.MySample;
import spikes.vitor.security.PolicySpike;
import topten.TopTen;

public class ContainerTest extends BrickTestSupport {

	@Brick
	private Container container;
	
	@Test
	public void testAssingable() {
		assertTrue(Object.class.isAssignableFrom(String.class));
		assertTrue(Object.class.isAssignableFrom(Integer.class));
		assertTrue(Number.class.isAssignableFrom(Integer.class));
		assertTrue(Container.class.isAssignableFrom(SimpleContainer.class));
	}
	
	@Test
	public void testBinder() throws Exception {
		Binder binder = new SimpleBinder();
		binder.bind(Sample.class).to(MySample.class);
		Container c = new SimpleContainer(binder);
		Sample sample = c.produce(Sample.class);
		assertTrue(sample instanceof MySample);
	}
	
	@Ignore
	public void testX() throws Exception {
		
		Policy.setPolicy(new PolicySpike());
		System.setSecurityManager(new SecurityManager());

		IM im = container.produce(IM.class);
		im.sendMessage("leandro", "Eu te amo. PS: Klaus");
		TopTen topTen = container.produce(TopTen.class);
		topTen.toString();
	}
	
}
