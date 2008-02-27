package sneer.lego.tests;

import static org.junit.Assert.assertTrue;
import im.IM;

import java.security.Policy;

import org.junit.Ignore;
import org.junit.Test;

import sneer.lego.Brick;
import sneer.lego.Container;
import sneer.lego.impl.SimpleContainer;
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
