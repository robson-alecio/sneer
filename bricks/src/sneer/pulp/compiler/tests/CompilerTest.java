package sneer.pulp.compiler.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;

import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;
import sneer.pulp.classpath.Classpath;
import sneer.pulp.classpath.ClasspathFactory;
import sneer.pulp.compiler.CompilationError;
import sneer.pulp.compiler.Result;
import wheel.lang.exceptions.NotImplementedYet;

public class CompilerTest extends BrickTestSupport {

	@Inject
	private sneer.pulp.compiler.Compiler _compiler;
	
	@Inject
	private ClasspathFactory _factory;
	
	@Test
	public void testCompile() throws Exception {
		Result result = compile("class Foo {}", null);
		assertTrue(result.getErrorString(), result.success());
		Assert.fail("Refactor tmp directory creation");
	}
	
	@Test
	public void testBadCode() throws Exception {
		Result result = compile("bricks/compiler/test-resources/badSample", null);
		assertFalse(result.success());
		CompilationError error = result.getErrors().get(0);
		assertEquals(3, error.getLineNumber());
		assertEquals("<identifier> expected", error.getMessage());
		assertEquals("BadSample.java", FilenameUtils.getName(error.getFileName()));
	}

	@Test
	public void testEmptyDir() throws Exception {
		Result result = compile("bricks/compiler/test-resources/empty", null);
		assertFalse(result.success());
	}

	@Test
	public void testWithExternalDependencies() throws Exception {
		Result result = compile("bricks/compiler/test-resources/externalDependencies/src", "bricks/compiler/test-resources/externalDependencies/lib");
		assertTrue(result.success());
	}
	
	private Result compile(String code, String libs) {
		String srcDir = FilenameUtils.concat("tmp", "compiler-test");
		File java = writeSourceFile(srcDir, code);
		Classpath classpath = classPathForLibs(libs);
		return _compiler.compile(Collections.singletonList(java), getWorkDirectory(), classpath);
	}

	private File writeSourceFile(String srcDir, String code) {
		File java = new File(srcDir, "Source.java"); 
		try {
			FileUtils.writeStringToFile(java, code);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return java;
	}

	private Classpath classPathForLibs(String libs) {
		if (libs == null)
			return _factory.newClasspath();
		
		throw new NotImplementedYet();
//		File libDir = null;
//		libDir = new File(FilenameUtils.concat(System.getProperty("user.dir"), libs));
//		Classpath classpath = _factory.fromLibDir(libDir);
//		return classpath;
	}
}
