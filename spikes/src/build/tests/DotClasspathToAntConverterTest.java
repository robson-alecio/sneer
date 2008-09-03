package build.tests;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import build.antFileGenerator.DotClasspathToAntConverter;
import build.dotClasspathParser.DotClasspathParser;
import build.tests.environment.FakeAntFileBuilder;


public class DotClasspathToAntConverterTest {

	@Test
	public void testGenerateAntBuild() throws IOException{
		
		final FakeAntFileBuilder fakeAntFileBuilder = new FakeAntFileBuilder();
		final String classpathString = DotClasspathToAntConverterTest.getClassPathWithTwoSrcTwoLibOneOutput();
		
		final DotClasspathToAntConverter generator = new DotClasspathToAntConverter(fakeAntFileBuilder);
		generator.createAntFile(DotClasspathParser.parse(classpathString));
		
		Assert.assertEquals(
				"lib bricks/lib/bcprov-jdk16-139.jar\n" +
				"lib kernel/lib/asm-all-3.1.jar\n" +
				"compile wheel/src\n" +
				"compile spikes/src",
				fakeAntFileBuilder.getStatements());
				
	}
	
	static String getClassPathWithTwoSrcTwoLibOneOutput() throws IOException {
		final InputStream resourceAsStream = 
			DotClasspathToAntConverterTest.class
				.getResourceAsStream("subjectClassPathTemplate");
		
		final String classpathWithTwoSrcTwoLibOneOutput = IOUtils.toString(resourceAsStream);
		return classpathWithTwoSrcTwoLibOneOutput;
	}
	
}
