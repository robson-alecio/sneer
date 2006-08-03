package spikes.klaus.crypto;

import java.security.Provider;
import java.security.Security;
import java.security.Provider.Service;
import java.util.HashSet;
import java.util.Set;

public class Main {

	public static void main(String[] args) {

		printProvidersAndServices();
		System.out.println("\n\n");

		String priv = "c:/temp/privateKey";
		String pub = "c:/temp/publicKey";
		String message = "c:/temp/message";
		String signature = "c:/temp/signature";
		
		long t0;

//		t0 = System.nanoTime();
//		KeyTools.main(new String[]{pub, priv});
//		System.out.println(System.nanoTime() - t0);
		
		t0 = System.nanoTime();
		Sign.main(new String[]{priv, message, signature});
		System.out.println(System.nanoTime() - t0);
		
		t0 = System.nanoTime();
		Verify.main(new String[]{pub, message, signature});
		System.out.println(System.nanoTime() - t0);

	}

	private static void printProvidersAndServices() {
		HashSet<String> serviceTypes = new HashSet<String>();

		Provider[] provs = Security.getProviders();
		for (Provider prov : provs) {
			System.out.println();
			System.out.println("===========" + prov);
			Set<Service> services = prov.getServices();
			for (Service service : services) {
				String type = service.getType();
				serviceTypes.add(type);
				System.out.println("\n Type: " + type);
				System.out.println(" Algo: " + service.getAlgorithm());
			}
		}

		for (String type : serviceTypes) {
			System.out.println("\n=====Type: " + type);
			for (String alg : Security.getAlgorithms(type)) {
				System.out.println(alg);
			}
		}
	}

}
