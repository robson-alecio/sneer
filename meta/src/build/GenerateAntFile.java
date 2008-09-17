package build;

import java.io.IOException;

import wheel.io.files.impl.DurableDirectory;
import build.antFileGenerator.AntFileBuilderToFilesystem;
import build.antFileGenerator.DotClasspathToAntConverter;
import build.dotClasspathParser.DotClasspath;
import build.dotClasspathParser.DotClasspathParser;

public class GenerateAntFile {

	private static final String CLASSPATH = ".classpath";

	public static void main(final String[] args) throws IOException {
		
		if (args.length != 1){
			System.out.println("Usage: GenerateAntFile -sourcefolderstogether|-separatedsourcefolders\n the program assumes that there is a .classpath file in current directory");
			System.exit(0);
		}
		
		final DurableDirectory directory = new DurableDirectory(".");
		
		if (directory.fileExists("build.xml"))
			directory.deleteFile("build.xml");
		
		if (!directory.fileExists(CLASSPATH))
			throw new IllegalStateException("File .classpath does not exist");
		
		System.out.println("generating build.xml");
		final String dotClasspathAsString = directory.contentsAsString(CLASSPATH);
		
		final DotClasspath classpath = DotClasspathParser.parse(dotClasspathAsString);
		
		boolean compileSourceFoldersTogether = args[0].equals("-sourcefolderstogether");
		AntFileBuilderToFilesystem antFileBuilder = new AntFileBuilderToFilesystem(directory,compileSourceFoldersTogether);
		final DotClasspathToAntConverter converter = new DotClasspathToAntConverter(antFileBuilder);
		
		converter.createAntFile(classpath);
		
		System.out.println("build.xml written");
		
	}

	

	
	
	

}
