package sneer.software.compilers.java.tests;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.software.compilers.classpath.Classpath;
import sneer.software.compilers.classpath.ClasspathFactory;
import sneer.software.compilers.java.CompilationError;
import sneer.software.compilers.java.JavaCompiler;
import sneer.software.compilers.java.Result;
import wheel.io.Jars;

public class JavaCompilerTest extends BrickTest {

	private static final String TEST_FILE_PREFIX = "sneer-test-";

	private JavaCompiler _compiler = my(JavaCompiler.class);
	
	private final ClasspathFactory _factory = my(ClasspathFactory.class);
	
	@Test
	public void testCompile() throws Exception {
		Result result = compile("class Foo {}", null);
		assertSuccess(result);
	}

	@Test
	public void testBadCode() throws Exception {
		Result result = compile("\nclass \n { public void foo() {} }", null);
		assertFalse(result.success());
		CompilationError error = result.getErrors().get(0);
		assertEquals(2, error.getLineNumber());
		assertEquals("<identifier> expected", error.getMessage());
		assertTrue(FilenameUtils.getName(error.getFileName()).startsWith(TEST_FILE_PREFIX));
	}

	@Test
	public void testEmptyDir() throws Exception {
		Result result = compile("bricks/compiler/test-resources/empty", null);
		assertFalse(result.success());
	}

	@Test
	public void testWithExternalDependencies() throws Exception {
		final File libFolder = createLibFolder();
		try {
			Jars.createJar(new File(libFolder, "lib.jar"), TestLib.class);
			Result result = compile("class Foo extends " + TestLib.class.getName() + " {}", libFolder);
			assertSuccess(result);
		} finally {
			FileUtils.deleteDirectory(libFolder);
		}
	}
		
	private void assertSuccess(Result result) {
		assertTrue(result.getErrorString(), result.success());
	}
	
	private File createLibFolder() {
		final File dir = new File(tmpDirectory(), "lib");
		dir.mkdirs();
		return dir;
	}

	private Result compile(String code, File libDir) {
		File java = writeSourceFile(code);
		Classpath classpath = classPathForLibs(libDir);
		return _compiler.compile(Collections.singletonList(java), tmpDirectory(), classpath);
	}

	private File writeSourceFile(String code) {
		try {
			File java = createTempFile(); 
			FileUtils.writeStringToFile(java, code);
			return java;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
	}

	private File createTempFile() throws IOException {
		return File.createTempFile(TEST_FILE_PREFIX, ".java", tmpDirectory());
	}

	private Classpath classPathForLibs(File libDir) {
		if (libDir == null)
			return _factory.newClasspath();
		
		return _factory.fromJarFiles(listJarFiles(libDir));
	}

	private File[] listJarFiles(File libDir) {
		return libDir.listFiles(new FilenameFilter() { @Override public boolean accept(File dir, String name) {
			return name.endsWith(".jar");
		}});
	}
}
