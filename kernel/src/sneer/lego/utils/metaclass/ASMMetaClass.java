package sneer.lego.utils.metaclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * @deprecated really slow
 */
class ASMMetaClass extends MetaClassSupport {

	private boolean _loaded = false;

	private ClassVisitor _visitor = new MyVisitor();
	
	public ASMMetaClass(File root, File classFile) {
		super(root, classFile);
	}

	@Override
	public String getName() {
		lazyLoad();
		return _className;
	}

	@Override
	public String getPackageName() {
		lazyLoad();
		return _packageName;
	}

	@Override
	public boolean isInterface() {
		lazyLoad();
		return _isInterface;
	}


	@Override
	protected void parse() {
		//do nothing
	}

	private void lazyLoad() {

		if (_loaded)
			return;

		//System.out.println("loading meta class from: "+_classFile.getName());
		InputStream is = null;
		try {
			is = new FileInputStream(_classFile);
			ClassReader reader = new ClassReader(is);
			reader.accept(_visitor, 0);
			_loaded = true;
		} catch (IOException e) {
			throw new MetaClassException("Error reading meta class from: " + _classFile, e);
		} finally {
			if (is != null)
				IOUtils.closeQuietly(is);
		}
	}

	class MyVisitor extends EmptyVisitor {
		@Override
		public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
			_isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
			_className = name.replaceAll("/", ".");
			_packageName = _className.substring(0, _className.lastIndexOf("."));
		}
	}
}
