package sneer.bricks.compiler.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.classpath.Classpath;
import sneer.bricks.classpath.ClasspathFactory;
import sneer.bricks.compiler.CompilationError;
import sneer.bricks.compiler.Result;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;

//@Ignore
public class CompilerTest extends BrickTestSupport {

	@Inject
	private sneer.bricks.compiler.Compiler _compiler;
	
	@Inject
	private ClasspathFactory _factory;
	
	@Test
	public void testCompile() throws Exception {
		Result result = compile("bricks/compiler/test-resources/sample", null);
		assertTrue(result.success());
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
	
	private Result compile(String sources, String libs) {
		String src = FilenameUtils.concat(System.getProperty("user.dir"), sources);
		File libDir = null; 
		if(libs != null) {
			libDir = new File(FilenameUtils.concat(System.getProperty("user.dir"), libs));
		}
		Classpath classpath = _factory.fromLibDir(libDir);
		return _compiler.compile(new File(src), getWorkDirectory(), classpath);
	}
}
