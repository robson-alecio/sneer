package sneer.pulp.compiler.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;
import sneer.pulp.classpath.Classpath;
import sneer.pulp.classpath.ClasspathFactory;
import sneer.pulp.compiler.CompilationError;
import sneer.pulp.compiler.Result;
import wheel.io.Jars;

public class CompilerTest extends BrickTestSupport {

	private static final String TEST_FILE_PREFIX = "sneer-test-";

	@Inject
	private sneer.pulp.compiler.Compiler _compiler;
	
	@Inject
	private ClasspathFactory _factory;
	
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
			Result result = compile("class Foo extends sneer.pulp.compiler.tests.TestLib {}", libFolder);
			assertSuccess(result);
		} finally {
			FileUtils.deleteDirectory(libFolder);
		}
	}
		
	private void assertSuccess(Result result) {
		assertTrue(result.getErrorString(), result.success());
	}
	
	private File createLibFolder() throws IOException {
		final File dir = new File(tmpDirectory(), "lib");
		dir.mkdirs();
		return dir;
	}

	private File tmpDirectory() throws IOException {
		return createTempFile().getParentFile();
	}

	private Result compile(String code, File libDir) {
		File java = writeSourceFile(code);
		Classpath classpath = classPathForLibs(libDir);
		return _compiler.compile(Collections.singletonList(java), getWorkDirectory(), classpath);
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
		return File.createTempFile(TEST_FILE_PREFIX, ".java");
	}

	private Classpath classPathForLibs(File libDir) {
		if (libDir == null)
			return _factory.newClasspath();
		
		return _factory.fromJarFiles(listJarFiles(libDir));
	}

	private List<File> listJarFiles(File libDir) {
		return Arrays.asList(libDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		}));
	}
}
