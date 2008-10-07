package main;

import javax.swing.SwingUtilities;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import wheel.io.Logger;

public class MainDemo {

	public static void main(String[] args) {
		try {
			tryToRun(args);
		} catch (Exception e) {
			System.err.println("Usage: MainDemo yourOwnName dynDnsUser dnyDnsPassword\n");
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void tryToRun(String[] args) throws Exception {
		Logger.redirectTo(System.out);
		
		demo().start(ownName(args), dynDnsUser(args), dynDnsPassword(args));
		waitUntilTheGuiThreadStarts();
	}

	private static MainDemoBrick demo() {
		Container container = ContainerUtils.newContainer();
		return container.produce(MainDemoBrick.class);
	}
	
	private static String ownName(String[] args) {
		return args[0];
	}

	private static String dynDnsUser(String[] args) {
		return args[1];
	}

	private static String dynDnsPassword(String[] args) {
		return args[2];
	}
	
	private static void waitUntilTheGuiThreadStarts() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable(){@Override public void run() {}});
	}
}
