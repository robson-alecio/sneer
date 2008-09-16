package main;

import javax.swing.SwingUtilities;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.dyndns.ownaccount.DynDnsAccount;
import wheel.io.Logger;

public class MainDemo {

	public static void main(String[] args) {
		try {
			tryToRun(args);
		} catch (Exception e) {
			System.err.println("Usage: MainDemo yourOwnName port dynDnsHost dynDnsUser dnyDnsPassword\n");
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void tryToRun(String[] args) throws Exception {
		Logger.redirectTo(System.out);
		
		demo().start(ownName(args), port(args), dynDnsAccount(args));
		waitUntilTheGuiThreadStarts();
	}

	private static String ownName(String[] args) {
		return args[0];
	}

	private static MainDemoBrick demo() {
		Container container = ContainerUtils.newContainer();
		return container.produce(MainDemoBrick.class);
	}

	private static int port(String[] args) {
		return Integer.parseInt(args[1]);
	}

	private static DynDnsAccount dynDnsAccount(String[] args) {
		return new DynDnsAccount(args[2], args[3], args[4]);
	}

	private static void waitUntilTheGuiThreadStarts() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable(){@Override public void run() {}});
	}
}
