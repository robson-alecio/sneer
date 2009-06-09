package sneer.foundation.brickness;

public class BrickConventions {

	private static final String IMPL_PACKAGE = ".impl";

	public static String implClassNameFor(String brickName) {
		int index = brickName.lastIndexOf(".");
		return brickName.substring(0, index) + IMPL_PACKAGE + brickName.substring(index) + "Impl";
	}

	public static String implPackageFor(String brickName) {
		return brickName.substring(0, brickName.lastIndexOf(".")) + IMPL_PACKAGE;
	}

}
