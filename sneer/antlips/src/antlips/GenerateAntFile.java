package antlips;

import java.io.IOException;

import wheel.io.files.impl.DurableDirectory;
import antlips.antFileGenerator.AntFileBuilderToFilesystem;
import antlips.antFileGenerator.DotClasspathToAntConverter;
import antlips.dotClasspathParser.DotClasspath;
import antlips.dotClasspathParser.DotClasspathParser;

public class GenerateAntFile {

	private static final String CLASSPATH = ".classpath";

	public static void main(final String[] args) throws IOException {
		
		if (args.length != 1){
			System.out.println("Usage: GenerateAntFile -sourcefolderstogether|-separatedsourcefolders\n the program assumes that there is a .classpath file in current directory");
			System.exit(0);
		}
		
		final DurableDirectory directory = new DurableDirectory(".");
		
		if (directory.fileExists(AntFileBuilderToFilesystem.FILENAME))
			directory.deleteFile(AntFileBuilderToFilesystem.FILENAME);
		
		if (!directory.fileExists(CLASSPATH))
			throw new IllegalStateException("File .classpath does not exist");
		
		System.out.println("generating " + AntFileBuilderToFilesystem.FILENAME);
		final String dotClasspathAsString = directory.contentsAsString(CLASSPATH);
		
		final DotClasspath classpath = DotClasspathParser.parse(dotClasspathAsString);
		
		boolean compileSourceFoldersTogether = args[0].equals("-sourcefolderstogether");
		AntFileBuilderToFilesystem antFileBuilder = new AntFileBuilderToFilesystem(directory,compileSourceFoldersTogether);
		final DotClasspathToAntConverter converter = new DotClasspathToAntConverter(antFileBuilder);
		
		converter.createAntFile(classpath);
		
		System.out.println(AntFileBuilderToFilesystem.FILENAME + " written");
		
	}

	

	
	
	

}
