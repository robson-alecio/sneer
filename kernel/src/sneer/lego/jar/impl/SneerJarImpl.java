package sneer.lego.jar.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

import sneer.bricks.crypto.Crypto;
import sneer.bricks.crypto.Digester;
import sneer.lego.Inject;
import sneer.lego.jar.SneerJar;
import sneer.lego.utils.InjectedBrick;
import wheel.io.JarBuilder;
import wheel.lang.exceptions.NotImplementedYet;

public class SneerJarImpl implements SneerJar {

	private static final long serialVersionUID = 1L;

	private JarBuilder _jarBuilder;

	private File _file;
	
	private Properties _properties;

	private byte[] _sneer1024;

	private byte[] _contents;
	
	transient private JarFile _jarFile;
	
	@Inject
	static private Crypto _crypto;

	public SneerJarImpl(File file) {
		_file = file;
	}

	public SneerJarImpl(File file, JarFile jarFile) {
		_jarFile = jarFile;
		_file = file;
	}

	private JarFile jarFile() {
		if(_jarFile != null)
			return _jarFile;

		try {
			String fileName = brickName() + "-" + role() + "-";
			File tmp = File.createTempFile(fileName, ".jar");
			IOUtils.write(_contents, new FileOutputStream(tmp));
			_contents = null;
			_file = tmp;
			_jarFile = new JarFile(_file);
		} catch(IOException e) {
			throw new NotImplementedYet(e);
		}
		return _jarFile;
	}

	public void add(String entryName, File file) throws IOException {
		builder().add(entryName, file);
	}

	public void add(String entryName, String contents) throws IOException {
		builder().add(entryName, contents);
	}

	private JarBuilder builder() throws IOException {
		if(_jarBuilder != null)
			return _jarBuilder;
		
		_jarBuilder = new JarBuilder(_file);
		return _jarBuilder;
	}

	@Override
	public void close() throws IOException {
		closeJarBuilder();
		_jarFile = new JarFile(_file);
	}

	private void closeJarBuilder() {
		if (_jarBuilder != null) {
			_jarBuilder.close();
			_jarBuilder = null;
		}
	}

	@Override
	public InputStream getInputStream(String entryName) throws IOException {
		return jarFile().getInputStream(jarFile().getEntry(entryName));
	}

	public Enumeration<JarEntry> entries() {
		return jarFile().entries();
	}

	@Override
	public byte[] sneer1024() {
		if(_sneer1024 != null) 
			return _sneer1024;
		
		String role = role();
		if("api".equals(role) || "impl".equals(role)) {
			_sneer1024 = makeHash();
		} else {
			_sneer1024 = "NO DONUT FOR YOU".getBytes();
		}
		return _sneer1024;
	}

	private byte[] makeHash() {
		Digester digester = _crypto.digester();
		Enumeration<JarEntry> e = jarFile().entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();
			String name = entry.getName();
			if (!entry.isDirectory() && includeInHash(name)) {
				InputStream is = null;
				try {
					is = getInputStream(name);
					//System.out.print("  " + name);
					digester.update(is);
				} catch (IOException ioe) {
					throw new wheel.lang.exceptions.NotImplementedYet(ioe); // Implement Handle this exception.
				} finally {
					if(is != null)
						IOUtils.closeQuietly(is);
				}
			}
		}
		return digester.digest();
	}

	private boolean includeInHash(String name) {
		String role = role();
		if("api".equals(role)) {
			return name.endsWith(".class") && name.indexOf("impl") < 0;
		} else if("impl".equals(role)) {
			return name.endsWith(".class") && name.indexOf("impl") >= 0;
//		} else if("api-src".equals(role)) {
//			return name.endsWith(".java") && name.indexOf("impl") < 0;
//		} else if("impl-src".equals(role)) {
//			return name.endsWith(".java") && name.indexOf("impl") >= 0;
		} else {
			throw new NotImplementedYet();
		}
	}

	@Override
	public File file() {
		return _file;
	}

	@Override
	public void explode(File target) throws IOException {
		Enumeration<JarEntry> e = jarFile().entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();
			String name = entry.getName();
			if(entry.isDirectory() && !skipDirectory(name)) {
				File dir = new File(target, name);
				dir.mkdirs();
			} else if (!entry.isDirectory() && !skipFile(name)) {
				File file = new File(target, name);
				FileUtils.touch(file);
				InputStream is = getInputStream(name);
				IOUtils.copy(is, new FileOutputStream(file));
				IOUtils.closeQuietly(is);
			}
		}
	}
	
	private boolean skipFile(String name) {
		return name.endsWith("MANIFEST.MF") || name.endsWith("sneer.meta");
	}

	private boolean skipDirectory(String name) {
		return name.endsWith("META-INF/");
	}
	
	private Properties properties() throws IOException {
		
		if(_properties != null)
			return _properties;
		
		Properties result = new Properties();
		InputStream is = getInputStream("sneer.meta"); 
		result.load(is);
		IOUtils.closeQuietly(is);
		_properties = result;
		return result;
	}

	@Override
	public List<InjectedBrick> injectedBricks() throws IOException {
		List<InjectedBrick> result = new ArrayList<InjectedBrick>();
		Enumeration<JarEntry> e = jarFile().entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();
			String name = entry.getName();
			if (!entry.isDirectory() && name.endsWith(".class")) {
				InputStream is = getInputStream(name);
				List<InjectedBrick> injected = findInjectedBricksOnClass(is);
				if(injected.size() > 0)
					result.addAll(injected);
			}
		}
		return result;
	}	
	
//	private String toEntryName(String brickName) {
//		int index = brickName.lastIndexOf(".");
//		brickName = brickName.substring(0, index + 1) + "impl" + brickName.substring(index) + "Impl"; 
//		return brickName.replaceAll("\\.", "/") + ".class";
//	}

	private List<InjectedBrick> findInjectedBricksOnClass(InputStream is) throws IOException {
		ClassReader classReader = new ClassReader(is);
		DependencyExtractor extractor = new DependencyExtractor();
		classReader.accept(extractor, 0);
		return extractor.injectedBricks();
		
	}

	@Override
	public String brickName() {
		return property("brick-name");
	}
	
	@Override
	public String role() {
		return property("role").toLowerCase();
	}
	
	private String property(String propertyName) {
		try {
			return properties().getProperty(propertyName);
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
	}

	@Override
	public String toString() {
		return _file.toString();
	}

	@Override
	public void afterSerialize() {
		_contents = null;
	}

	@Override
	public void beforeSerialize() throws IOException {
		_contents = IOUtils.toByteArray(new FileInputStream(_file));
	}

//    public void copy(InputStream input, OutputStream output) throws IOException {
//    	byte[] bytes = new byte[BUFFER_SIZE];
//    	int n = 0;
//    	while (-1 != (n = input.read(bytes))) {
//    		output.write(bytes, 0, n);
//    		_buffer.put(bytes);
//    	}
//    }
}

class DependencyExtractor extends EmptyVisitor {

	private String _currentField;
	
	List<InjectedBrick> _injectedBricks = new ArrayList<InjectedBrick>();
	
	/*
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
		System.out.println("Class: "+name);
	}
	*/

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if(_currentField != null 
				&& Inject.class.getName().equals(Type.getType(desc).getClassName())) {
			String className = Type.getType(_currentField).getClassName();
			InjectedBrick dep = new InjectedBrick(className);
			_currentField = null;
			_injectedBricks.add(dep);
		}
		return super.visitAnnotation(desc, visible);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		_currentField = desc;
		return super.visitField(access, name, desc, signature, value);
	}
	
	public List<InjectedBrick> injectedBricks() {
		return _injectedBricks;
	}
}