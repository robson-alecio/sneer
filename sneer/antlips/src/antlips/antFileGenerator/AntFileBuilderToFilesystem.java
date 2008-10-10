package antlips.antFileGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import wheel.io.codegeneration.Template;
import wheel.io.files.Directory;
import wheel.lang.Pair;

public class AntFileBuilderToFilesystem implements AntFileBuilder {

	public static final String FILENAME = "antlips.xml";
	
	private static final String BUILD_DIR_PROPERTY = "build.dir";

	private static final String DEFAULT_DEST_DIR = "${" + BUILD_DIR_PROPERTY + "}";
	
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
		_srcs.add(Pair.pair(src, safeDestDir(output)));
	}

	@Override
	public void build() {
		try {
			final String script = generateAntScript();
			writeAntFile(script);
		} catch (final IOException e) {
			throw new IllegalStateException("Error creating file " + FILENAME, e);
		} 
	}

	private void writeAntFile(final String contents) throws IOException {
		final OutputStream file = createOrCry(_directory, FILENAME);
		try {
			IOUtils.write(contents, file);
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

	private String generateAntScript() throws IOException {
		return Template.evaluate(loadTemplateString(), this);
	}

	private String loadTemplateString() throws IOException {
		final String templateName = _compileSourceFoldersTogether
			? "source-folders-together.template"
			: "source-folders-separate.template";
		return IOUtils.toString(getClass().getResourceAsStream(templateName));
	}
	
	public Iterable<Pair<String, String>> srcs() {
		return _srcs;
	}

	public Iterable<String> destDirs() {
		if (_compileSourceFoldersTogether)
			return Arrays.asList(DEFAULT_DEST_DIR);
		final Set<String> destDirs = new HashSet<String>();
		for (Pair<String, String> src : _srcs)
			destDirs.add(src._b);
		return destDirs;
	}
	
	private String safeDestDir(final String destDir) {
		return StringUtils.isEmpty(destDir)
			? DEFAULT_DEST_DIR
			: destDir;
	}

	public List<String> libs() {
		return _libs;
	}

}
