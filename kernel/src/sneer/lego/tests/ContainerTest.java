package sneer.lego.tests;

import im.IM;

import java.security.Policy;

import org.junit.Ignore;
import org.junit.Test;

import sneer.lego.Brick;
import sneer.lego.Container;
import spikes.vitor.security.PolicySpike;
import topten.TopTen;

public class ContainerTest extends BrickTestSupport {

	@Brick
	private Container container;
	
	@Test
	public void testProduce() throws Exception {
		SampleBrick sample = container.produce(SampleBrick.class);
		sample.toString();
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
