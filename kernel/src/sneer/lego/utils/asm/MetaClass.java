package sneer.lego.utils.asm;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;


public class MetaClass extends EmptyVisitor {

	private boolean _isInterface;
	
	private String _className;
	
	private File _classFile;

	public MetaClass(File classFile) {
		_classFile = classFile;
	}

	public String getName() {
		return _className;
	}

	public File classFile() {
		return _classFile;
	}

	public byte[] bytes() throws IOException {
		return FileUtils.readFileToByteArray(_classFile);
	}

	public boolean isInterface() {
		return _isInterface;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		_isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
		_className = name.replaceAll("/", ".");
	}
}
