package sneer.brickness.environment;

public class BrickConventions {

	private static final String IMPL_PACKAGE = ".impl";

	public static String implClassNameFor(String brickInterface) {
		int index = brickInterface.lastIndexOf(".");
		return brickInterface.substring(0, index) + IMPL_PACKAGE + brickInterface.substring(index) + "Impl";
	}

	public static String implPackageFor(String brickPackage) {
		return brickPackage + IMPL_PACKAGE;
	}

}
