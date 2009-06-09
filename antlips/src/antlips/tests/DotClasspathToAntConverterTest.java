package antlips.tests;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import antlips.antFileGenerator.AntUtils;
import antlips.antFileGenerator.DotClasspathToAntConverter;
import antlips.dotClasspathParser.DotClasspathParser;
import antlips.tests.environment.FakeAntFileBuilder;


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
				"compile wheel/src to sneerAPI-bin\n" +
				"compile spikes/src",
				fakeAntFileBuilder.getStatements());
				
	}
	
	static String getClassPathWithTwoSrcTwoLibOneOutput() throws IOException {
		final InputStream resourceAsStream = 
			DotClasspathToAntConverterTest.class
				.getResourceAsStream("subjectClassPathTemplate");
		
		final String classpathWithTwoSrcTwoLibOneOutput = AntUtils.readString(resourceAsStream);
		return classpathWithTwoSrcTwoLibOneOutput;
	}
	
}
