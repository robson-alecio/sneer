package spikes.wheel.io.ui;

public class Util {
	
	public static String workAroundSwingNewlineSpaceProblem(String message) {
		return " " + message.replaceAll("\\n", "\n ");
	}
	
}
