package build.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;
import build.antFileGenerator.DotClasspathToAntConverter;
import build.dotClasspathParser.DotClasspath;


public class AntFileBuilderTests {

	@Test
	public void testAntFileBuilder() throws IOException{
		
		final Directory directory = new TransientDirectory();

		final DotClasspath classpath = 
			new DotClasspath(
				Arrays.asList("spikes/src", "wheel/src"),
				Arrays.asList("kernel/lib/asm-all-3.1.jar", "bricks/lib/bcprov-jdk16-139.jar"));
				
		
		new DotClasspathToAntConverter(directory).createAntFile(classpath);
		
		final String subjectAntFileTemplate = getSubjectAntFile();
		final String generated = directory.contentsAsString("build.xml");
		Assert.assertEquals(subjectAntFileTemplate, generated);
		
	}
	
	static String getSubjectAntFile() throws IOException {
		final InputStream resourceAsStream = 
			DotClasspathToAntConverterTest.class
				.getResourceAsStream("subject_build.xml.template");
		
		final String classpathWithTwoSrcTwoLibOneOutput = IOUtils.toString(resourceAsStream);
		return classpathWithTwoSrcTwoLibOneOutput;
	}
}
