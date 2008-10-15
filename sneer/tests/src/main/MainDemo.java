package main;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import wheel.io.Logger;
import wheel.io.ui.GuiThread;

public class MainDemo {

	public static void main(String[] args) {
		try {
			tryToRun(args);
		} catch (Exception e) {
			System.err.println(e.getClass() + " " + e.getMessage());
			System.err.println("\nUsage: MainDemo yourOwnName [dynDnsUser dnyDnsPassword]\n");
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
		if (args.length < 2) return null;
		return args[1];
	}

	private static String dynDnsPassword(String[] args) {
		if (args.length < 2) return null;
		return args[2];
	}
	
	private static void waitUntilTheGuiThreadStarts() throws Exception {
		GuiThread.strictInvokeAndWait(new Runnable(){@Override public void run() {}});
	}
}
