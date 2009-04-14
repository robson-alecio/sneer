package wheel.io.codegeneration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


public abstract class MetaClassSupport implements MetaClass {

	protected File _classFile;

	protected File _root;
	
	protected String _className;

	protected String _packageName;

	protected boolean _isInterface;

	public MetaClassSupport(File root, File classFile) {
		_root = root;
		_classFile = classFile;
		parse();
	}

	protected abstract void parse();

	@Override
	public String getName() {
		return _className;
	}

	@Override
	public String getPackageName() {
		return _packageName;
	}

	@Override
	public boolean isInterface() {
		return _isInterface;
	}
	
	@Override
	public byte[] bytes() throws IOException {
		return FileUtils.readFileToByteArray(_classFile);
	}

	@Override
	public File classFile() {
		return _classFile;
	}
	
	@Override
	public String toString() {
		return _className + " @" + _classFile;
	}

}
