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
		String dotClasspathAsString = directory.contentsAsString(CLASSPATH);
//		System.out.println(dotClasspathAsString);
		
		final String lines[] = dotClasspathAsString.split("\n");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			if(lines[i].indexOf("spikes\\src")<0 
			&& lines[i].indexOf("spikes/src")<0
			&& lines[i].indexOf("spikes/lib")<0
			&& lines[i].indexOf("spikes\\lib")<0)
				builder.append(lines[i]).append("\n");
		}
//		System.out.println("****************************************************");
		
		dotClasspathAsString = builder.toString();	
//		System.out.println(dotClasspathAsString);
		
		final DotClasspath classpath = DotClasspathParser.parse(dotClasspathAsString);
		
		boolean compileSourceFoldersTogether = args[0].equals("-sourcefolderstogether");
		AntFileBuilderToFilesystem antFileBuilder = new AntFileBuilderToFilesystem(directory,compileSourceFoldersTogether);
		final DotClasspathToAntConverter converter = new DotClasspathToAntConverter(antFileBuilder);
		
		converter.createAntFile(classpath);
		
		System.out.println("build.xml written");
		
	}

	

	
	
	

}
