package build.antFileGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import wheel.io.files.Directory;

public class AntFileBuilderToFilesystem implements AntFileBuilder {

	private final List<String> _libs = new ArrayList<String>();
	private final List<String> _srcs = new ArrayList<String>();
	private final Directory _directory;

	public AntFileBuilderToFilesystem(final Directory directory) {
		_directory = directory;
	}

	@Override
	public void addClasspathEntry(final String lib) {
		_libs.add(lib);
	}

	@Override
	public void addCompileEntry(final String src) {
		_srcs.add(src);
	}

	@Override
	public void build() {
		final StringBuilder builder = generateAntFile();
		
		final String fileName = "build.xml";
		final OutputStream file = createOrCry(_directory, fileName);
		try {
			IOUtils.write(builder.toString(), file);
		} catch (final IOException e) {
			throw new IllegalStateException("Error creating file " + fileName, e);
		} finally {
			IOUtils.closeQuietly(file);
		}
	}

	//refactor: move to directory
	private static OutputStream createOrCry(final Directory directory, final String fileName) {
		try {
			return directory.createFile(fileName);
		} catch (final IOException e) {
			throw new RuntimeException("Error opening " + fileName, e);
		}
	}

	private StringBuilder generateAntFile() {
		final StringBuilder builder = new StringBuilder();
		
		builder.append(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
		"<project name=\"AntBuild\" default=\"compile\">\n" +
		"\n"));
		builder.append("\t<path id=\"classpath\">\n");
		builder.append("\t\t<pathelement path=\"distr/bin\"/>\n");
		for (final String lib : _libs)
			builder.append("\t\t<pathelement path=\"" + lib + "\"/>\n");
		builder.append("\t</path>\n");
		
		builder.append("\n");
		
		builder.append("\t<target name=\"compile\">\n");
		for (final String src : _srcs){
			builder.append(	
					"\t\t<javac srcdir=\""+ src +"\"\n"+
						"\t\t\t\tdestdir=\"distr/bin\"\n" +
						"\t\t\t\tlistfiles=\"true\"\n" +
						"\t\t\t\tfailonerror=\"true\"\n" +
						"\t\t\t\tdebug=\"on\"\n" +
						"\t\t\t\ttarget=\"1.5\"\n" +
						"\t\t>\n" +
							"\t\t\t<classpath refid=\"classpath\"/>\n" +
						"\t\t</javac>\n");		      
		}
		builder.append("\t</target>\n");
		builder.append("</project>");
		return builder;
	}

}