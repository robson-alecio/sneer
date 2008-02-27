import im.IM;

import java.security.Policy;

import spikes.lego.Container;
import spikes.lego.ContainerUtils;
import spikes.vitor.security.PolicySpike;
import topten.TopTen;


public class Main {
	
	public static void main(String[] args) throws Exception {
		Policy.setPolicy(new PolicySpike());
		System.setSecurityManager(new SecurityManager());

		Container container = ContainerUtils.getContainer();
		
		IM im = container.produce(IM.class);
		im.sendMessage("leandro", "Eu te amo. PS: Klaus");
		TopTen topTen = container.produce(TopTen.class);
	}
}
