package main;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.own.name.OwnNameKeeper;
import wheel.io.Logger;
import wheel.io.ui.GuiThread;

public class MainDemo {

	private static Container _container;

	public static void main(String[] args) {
		try {
			tryToRun(args);
		} catch (ArrayIndexOutOfBoundsException e) {
			exitWithUsageMessage();
		} catch (Exception e) {
			e.printStackTrace();
			exitWithUsageMessage();
		}
	}

	private static void exitWithUsageMessage() {
		System.err.println("\nUsage: MainDemo yourOwnName [dynDnsUser dnyDnsPassword]\n");
		System.exit(1);
	}

	private static void tryToRun(String[] args) throws Exception {
		Logger.redirectTo(System.out);
		
		setOwnName(ownName(args));
		demo().start(dynDnsUser(args), dynDnsPassword(args));
		
		waitUntilTheGuiThreadStarts();
	}

	private static void setOwnName(String ownName) {
		container().produce(OwnNameKeeper.class).nameSetter().consume(ownName);
	}

	private static Container container() {
		if (_container == null) _container = ContainerUtils.newContainer();
		return _container;
	}

	private static MainDemoBrick demo() {
		return container().produce(MainDemoBrick.class);
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
