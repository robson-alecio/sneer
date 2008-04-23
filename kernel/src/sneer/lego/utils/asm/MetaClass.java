package sneer.lego.utils.asm;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;


public class MetaClass extends EmptyVisitor {

	private boolean _isInterface;
	
	private String _className;
	
	private String _packageName;
	
	private File _classFile;
	
	private String[] _interfaces;
	
	public MetaClass(File classFile) {
		_classFile = classFile;
	}

	public String getName() {
		return _className;
	}

	public String getPackageName() {
		return _packageName;
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
		_packageName = _className.substring(0, _className.lastIndexOf("."));
		_interfaces = interfaces;
	}

	public boolean isAssignanbleTo(Class<?> clazz) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public String toString() {
		return _className +" @" + _classFile;
	}
}
