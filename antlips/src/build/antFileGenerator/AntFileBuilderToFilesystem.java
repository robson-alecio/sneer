package build.antFileGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import wheel.io.files.Directory;
import wheel.lang.Pair;

public class AntFileBuilderToFilesystem implements AntFileBuilder {

	public static final String FILENAME = "antlips.xml";
	
	private static final String BUILD_DIR_PROPERTY = "build.dir";
	private final List<String> _libs = new ArrayList<String>();
	private final List<Pair<String, String>> _srcs = new ArrayList<Pair<String, String>>();
	private final Directory _directory;
	private final boolean _compileSourceFoldersTogether;

	public AntFileBuilderToFilesystem(final Directory directory) {
		this(directory, false);
	}

	public AntFileBuilderToFilesystem(Directory directory, boolean compileSourceFoldersTogether) {
		_directory = directory;
		_compileSourceFoldersTogether = compileSourceFoldersTogether;
	}

	@Override
	public void addClasspathEntry(final String lib) {
		_libs.add(lib);
	}

	@Override
	public void addCompileEntry(final String src, final String output) {
		_srcs.add(Pair.pair(src, output));
	}

	@Override
	public void build() {
		final StringBuilder builder = generateAntFile();
		
		final OutputStream file = createOrCry(_directory, FILENAME);
		try {
			IOUtils.write(builder.toString(), file);
		} catch (final IOException e) {
			throw new IllegalStateException("Error creating file " + FILENAME, e);
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
		builder.append("\t<property name=\"" + BUILD_DIR_PROPERTY + "\" location=\"bin\" />\n\n");
		builder.append("\t<path id=\"classpath\">\n");
		builder.append("\t\t<pathelement path=\"${" + BUILD_DIR_PROPERTY + "}\"/>\n");
		for (final String lib : _libs)
			builder.append("\t\t<pathelement path=\"" + lib + "\"/>\n");
		builder.append("\t</path>\n");
		
		builder.append("\n");
		
		builder.append("\t<target name=\"compile\" depends='copyResources'>\n");
		builder.append("\t\t<mkdir dir=\"${" + BUILD_DIR_PROPERTY + "}\"/>\n");
		builder.append(appendJavacCall());
		builder.append("\t</target>\n\n");
		
		
		builder
		.append("\t<target name='copyResources'>\n")
			.append("\t\t<copy todir='${build.dir}'>\n");
					appendFileSetsToCopy(builder);
		builder
			.append("\t\t</copy>\n")
		.append("\t</target>\n");
		
		builder.append("</project>");
		return builder;
	}

	private void appendFileSetsToCopy(StringBuilder builder) {
		
		for (Pair<String, String> src : _srcs) {
			builder
			.append("\t\t\t<fileset dir='").append(src._a).append("'>\n")
			.append("\t\t\t\t<include name='**/**'/>\n")
			.append("\t\t\t\t<exclude name='**/*.java'/>\n")
			.append("\t\t\t</fileset>\n");
		}
	}

	private String appendJavacCall() {
		if (_compileSourceFoldersTogether)
			return getJavacCallsWithSourceFoldersTogether();
		
		return getJavacCallsWithSeparateSourceFolders();
	}

	private String getJavacCallsWithSourceFoldersTogether() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(	
				"\t\t<javac\n"+
					"\t\t\t\tdestdir=\"${" + BUILD_DIR_PROPERTY + "}\"\n" +
					"\t\t\t\tlistfiles=\"true\"\n" +
					"\t\t\t\tfailonerror=\"true\"\n" +
					"\t\t\t\tdebug=\"on\"\n" +
					"\t\t\t\ttarget=\"1.5\"\n" +
					"\t\t\t\tencoding=\"utf-8\"\n" +
					"\t\t>\n");
		
		for (final Pair<String, String> src : _srcs){
			builder.append("\t\t\t<src path=\"" + src._a + "\"/>\n");		      
		}
		
		builder.append("\t\t\t<classpath refid=\"classpath\"/>\n" +
						"\t\t</javac>\n");	
		return builder.toString();
	}

	private String getJavacCallsWithSeparateSourceFolders() {
		StringBuilder builder = new StringBuilder();
		for (final Pair<String, String> src : _srcs){
			final String destdir = StringUtils.isEmpty(src._b)
				? "${" + BUILD_DIR_PROPERTY + "}"
				: src._b;
			builder.append(	
					"\t\t<javac srcdir=\""+ src._a +"\"\n"+
						"\t\t\t\tdestdir=\"" + destdir + "\"\n" +
						"\t\t\t\tlistfiles=\"true\"\n" +
						"\t\t\t\tfailonerror=\"true\"\n" +
						"\t\t\t\tdebug=\"on\"\n" +
						"\t\t\t\ttarget=\"1.5\"\n" +
						"\t\t\t\tencoding=\"utf-8\"\n" +
						"\t\t>\n" +
							"\t\t\t<classpath refid=\"classpath\"/>\n" +
						"\t\t</javac>\n");		      
		}
		
		return builder.toString();
	}

}
