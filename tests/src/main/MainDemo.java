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
			System.err.println("Usage: MainDemo YourOwnName [port]\n");
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void tryToRun(String[] args) throws Exception {
		Logger.redirectTo(System.out);
		
		demo().start(ownName(args), port(args));
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
		return args.length < 2
			? 7777
			: Integer.parseInt(args[1]);
	}

	private static void waitUntilTheGuiThreadStarts() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable(){@Override public void run() {}});
	}
}
