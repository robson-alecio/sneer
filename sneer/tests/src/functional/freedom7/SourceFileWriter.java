package functional.freedom7;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class SourceFileWriter {

	private final File _root;

	public SourceFileWriter(File root) {
		_root = root;
	}

	public void write(String className, String code) throws IOException {
		FileUtils.writeStringToFile(javaFile(className), "package " + packageName(className) + ";\n" + code);
	}

	private String packageName(String className) {
		return className.substring(0, className.lastIndexOf('.'));
	}

	public File javaFile(String className) {
		return new File(_root, className.replace('.', '/') + ".java");
	}

}
