package sneer.kernel.container.jar.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

import sneer.kernel.container.Inject;
import sneer.kernel.container.jar.DeploymentJar;
import sneer.kernel.container.utils.InjectedBrick;
import sneer.pulp.crypto.Crypto;
import sneer.pulp.crypto.Digester;
import wheel.io.JarExploder;
import wheel.lang.Predicate;
import wheel.lang.exceptions.NotImplementedYet;

public class DeploymentJarImpl implements DeploymentJar {

	private static final long serialVersionUID = 1L;

	private final File _file;
	
	private Properties _properties;

	private byte[] _sneer1024;

	private byte[] _contents;
	
	transient private JarFile _jarFile;
	
	@Inject
	static private Crypto _crypto;

	public DeploymentJarImpl(File file) {
		_file = file;
	}

	private JarFile jarFile() throws IOException {
		if(_jarFile != null)
			return _jarFile;

		_jarFile = new JarFile(_file);
		
		return _jarFile;
	}


	@Override
	public void close() throws IOException {
		if (null != _jarFile) {
			_jarFile.close();
			_jarFile = null;
		}
	}

	private InputStream inputStreamFor(String entryName) throws IOException {
		return inputStreamFor(jarFile().getEntry(entryName));
	}

	private InputStream inputStreamFor(final ZipEntry entry) throws IOException {
		return jarFile().getInputStream(entry);
	}

	@Override
	public byte[] sneer1024() {
		if(_sneer1024 != null) 
			return _sneer1024;
		
		String role = role();
		if("api".equals(role) || "impl".equals(role)) {
			try {
				_sneer1024 = makeHash();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		} else {
			_sneer1024 = "NO DONUT FOR YOU".getBytes();
		}
		return _sneer1024;
	}

	private byte[] makeHash() throws IOException {
		Digester digester = _crypto.digester();
		Enumeration<JarEntry> e = jarFile().entries();
		while (e.hasMoreElements()) {
			final JarEntry entry = e.nextElement();
			if (entry.isDirectory())
				continue;
			final String name = entry.getName();
			if (!includeInHash(name))
				continue;
			updateHash(digester, entry);
		}
		return digester.digest();
	}

	private void updateHash(Digester digester, final JarEntry entry)
			throws IOException {
		InputStream is = inputStreamFor(entry);
		try {
			digester.update(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
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
		new JarExploder(jarFile(), target, new Predicate<JarEntry>() {
			public boolean evaluate(JarEntry entry) {
				if (entry.isDirectory())
					return skipDirectory(entry.getName());
				return skipFile(entry.getName());
			}
		}).explode();
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
		
		_properties = loadProperties();
		return _properties;
	}

	private Properties loadProperties() throws IOException {
		final Properties result = new Properties();
		final InputStream is = inputStreamFor("sneer.meta"); 
		try {
			result.load(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
		return result;
	}

	@Override
	public List<InjectedBrick> injectedBricks() throws IOException {
		final List<InjectedBrick> result = new ArrayList<InjectedBrick>();
		final Enumeration<JarEntry> e = jarFile().entries();
		while (e.hasMoreElements()) {
			final JarEntry entry = e.nextElement();
			if (entry.isDirectory())
				continue;
			final String name = entry.getName();
			if (name.endsWith(".class"))
				result.addAll(injectedBricksFor(entry));
		}
		return result;
	}

	private List<InjectedBrick> injectedBricksFor(JarEntry classEntry)
			throws IOException {
		final InputStream is = inputStreamFor(classEntry);
		try {
			return findInjectedBricksOnClass(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
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