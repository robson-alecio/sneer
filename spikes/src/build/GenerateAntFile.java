package build;

import java.io.IOException;

import wheel.io.files.impl.DurableDirectory;
import build.antFileGenerator.DotClasspathToAntConverter;
import build.dotClasspathParser.DotClasspath;
import build.dotClasspathParser.DotClasspathParser;

public class GenerateAntFile {

	private static final String CLASSPATH = ".classpath";

	public static void main(final String[] args) throws IOException {
		
		if (args.length != 0)
			System.out.println("Usage: GenerateAntFile \n the program assumes that there is a .classpath file and the isn't a build.xml in current directory");
		
		
		final DurableDirectory directory = new DurableDirectory(".");
		
		if (directory.fileExists("build.xml"))
			throw new IllegalStateException("File build.xml already exists");
		
		if (!directory.fileExists(CLASSPATH))
			throw new IllegalStateException("File .classpath does not exist");
		
		System.out.println("generating build.xml");
		final String dotClasspathAsString = directory.contentsAsString(CLASSPATH);
		final DotClasspath classpath = DotClasspathParser.parse(dotClasspathAsString);

		final DotClasspathToAntConverter converter = new DotClasspathToAntConverter(directory);
		
		converter.createAntFile(classpath);
		
	}

	

	
	
	

}
