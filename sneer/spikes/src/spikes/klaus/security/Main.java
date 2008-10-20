package spikes.klaus.security;

import java.security.Policy;

import spikes.vitor.security.PolicySpike;

public class Main extends ClassLoader {

	public static void main(String[] args) throws Exception {

		Policy.setPolicy(new PolicySpike());
		System.setSecurityManager(new SecurityManager());

		new HelloWorld();

	}

	

}
