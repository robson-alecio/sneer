package sneer.compiler.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import sneer.compiler.Result;
import sneer.lego.Brick;
import sneer.lego.tests.BrickTestSupport;

public class CompilerTest extends BrickTestSupport {

	@Brick
	private sneer.compiler.Compiler compiler;
	
	@Test
	public void testCompile() throws Exception {
		Result result = compile("bricks/compiler/test-resources/sample");
		assertTrue(result.success());
	}
	
	@Test
	public void testBadCode() throws Exception {
		Result result = compile("bricks/compiler/test-resources/badSample");
		assertFalse(result.success());
	}

	@Test
	public void testEmptyDir() throws Exception {
		Result result = compile("bricks/compiler/test-resources/empty");
		assertFalse(result.success());
	}
	
	private Result compile(String sources) {
		String fileName = FilenameUtils.concat(System.getProperty("user.dir"), sources);
		File src = new File(fileName);
		assertTrue("Can't find directory "+fileName, src.exists());
		File dst = getWorkDirectory();
		Result result = compiler.compile(src, dst);
		return result;
	}
}
