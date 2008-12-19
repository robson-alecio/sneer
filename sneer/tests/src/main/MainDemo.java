package main;

import java.io.File;

import sneer.kernel.container.Container;
import sneer.kernel.container.Containers;
import sneer.pulp.own.name.OwnNameKeeper;
import wheel.io.Logger;
import wheel.lang.Environments;

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
		System.err.println("\nUsage: MainDemo yourOwnName [dynDnsUser dnyDnsPassword]");
		System.err.println(  "   or: MainDemo Dummy\n");
		System.exit(1);
	}

	private static void tryToRun(final String[] args) throws Exception {
		Logger.redirectTo(System.out);

		checkForDummy(args);
		
		Environments.runWith(container(), new Runnable() { @Override public void run() {
			setOwnName(ownName(args));
			demo().start(dynDnsUser(args), dynDnsPassword(args));			
		}});
	}

	private static void checkForDummy(String[] args) {
		if (!"Dummy".equals(ownName(args))) return;
		
		File dummyHome = new File(System.getProperty("user.home"), ".sneerdummy");
		System.setProperty("home_override", dummyHome.getAbsolutePath());
	}

	private static void setOwnName(String ownName) {
		container().provide(OwnNameKeeper.class).nameSetter().consume(ownName);
	}

	private static Container container() {
		if (_container == null) _container = Containers.newContainer();
		return _container;
	}


	private static MainDemoBrick demo() {
		return container().provide(MainDemoBrick.class);
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
	
}
