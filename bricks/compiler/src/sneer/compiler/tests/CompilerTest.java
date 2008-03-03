package sneer.compiler.tests;

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

		String fileName = FilenameUtils.concat(System.getProperty("user.dir"), "bricks/compiler/test-resources/sample");
		File src = new File(fileName);
		assertTrue("Can't find directory "+fileName, src.exists());
		File dst = getWorkDirectory();
		Result result = compiler.compile(src, dst);
		assertTrue(result.success());
	}
}
