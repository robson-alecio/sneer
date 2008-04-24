package sneer.lego.utils.asm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;

class LazyMetaClass extends EmptyVisitor implements MetaClass {

    private boolean _isInterface;

    private String _className;

    private String _packageName;

    private File _classFile;

    private boolean _loaded = false;

    private File _root;

    public LazyMetaClass(File root, File classFile) {
	_root = root;
	_classFile = classFile;
    }

    public String getName() {
	load();
	return _className;
    }

    public String getPackageName() {
	load();
	return _packageName;
    }

    public boolean isInterface() {
	load();
	return _isInterface;
    }

    public File classFile() {
	return _classFile;
    }

    public byte[] bytes() throws IOException {
	return FileUtils.readFileToByteArray(_classFile);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
	    String superName, String[] interfaces) {
	_isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
	_className = name.replaceAll("/", ".");
	_packageName = _className.substring(0, _className.lastIndexOf("."));
    }

    private void load() {

	if (_loaded)
	    return;

	//System.out.println("loading meta class from: "+_classFile.getName());
	InputStream is = null;
	try {
	    is = new FileInputStream(_classFile);
	    ClassReader reader = new ClassReader(is);
	    reader.accept(this, 0);
	    _loaded = true;
	} catch (IOException e) {
	    throw new MetaClassException("Error reading meta class from: "
		    + _classFile, e);
	} finally {
	    if (is != null)
		IOUtils.closeQuietly(is);
	}
    }

    @Override
    public String futureClassName() {
	String rootPath = _root.getAbsolutePath();
	String path = _classFile.getAbsolutePath();
	if (!path.startsWith(rootPath))
	    throw new MetaClassException("Class file " + path
		    + " on wrong directory");

	String result = path.substring(rootPath.length() + 1);
	result = result.substring(0, result.indexOf("."));
	result = result.replaceAll("/", ".");
	return result;
    }

    @Override
    public String toString() {
	return _className + " @" + _classFile;
    }

}
