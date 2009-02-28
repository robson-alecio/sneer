package sneer.brickness;

public class Conventions {

	public static String implementationNameFor(String brickInterface) {
		int index = brickInterface.lastIndexOf(".");
		return brickInterface.substring(0, index) + ".impl" + brickInterface.substring(index) + "Impl";
	}

}
