package spikes.runner;

import java.io.File;

import sneer.main.Sneer;

public class SneerGit {

	public static void main(String[] args) throws Exception {
		System.setProperty("sneer.home", gitHome());
		
		Sneer.main(args);
	}

	private static String gitHome() {
		File result = ClassFiles.classpathRootFor(SneerGit.class); // repository/code/spikes/bin
		result = result.getParentFile().getParentFile().getParentFile();
		System.out.println(result);
		return result.getAbsolutePath();
	}

}
