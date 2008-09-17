package build.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;
import build.antFileGenerator.AntFileBuilderToFilesystem;
import build.antFileGenerator.DotClasspathToAntConverter;
import build.dotClasspathParser.DotClasspath;


public class AntFileBuilderTest {

	
	private Directory _directory;
	private DotClasspath _classpath;

	@Before
	public void setup(){
		_directory = new TransientDirectory();
		_classpath = new DotClasspath(
			Arrays.asList("spikes/src", "wheel/src"),
			Arrays.asList("kernel/lib/asm-all-3.1.jar", "bricks/lib/bcprov-jdk16-139.jar"));
	}
	
	@Test
	public void testAntFileBuilder() throws IOException{				
		
		new DotClasspathToAntConverter(_directory).createAntFile(_classpath);
		
		final String subjectAntFileTemplate = getSubjectAntFile();
		final String generated = _directory.contentsAsString("antlips.xml");
		
		compareLineByLine(generated, subjectAntFileTemplate);
	}

	@Test
	public void testAntFileBuilderCompileSourceFoldersTogether() throws IOException{				
		
		new DotClasspathToAntConverter(new AntFileBuilderToFilesystem(_directory, true)).createAntFile(_classpath);
		
		final String subjectAntFileTemplate = getSourceFoldersTogetherSubjectAntFile(); 
		final String generated = _directory.contentsAsString("antlips.xml");
		
		compareLineByLine(generated, subjectAntFileTemplate);
	}
	
	private void compareLineByLine(String expected, String found) {
		String expectedLines[] = expected.split("\n"); 
		String foundLines[] = found.split("\n");
		
		Assert.assertEquals(expectedLines.length, foundLines.length);
		
		for (int i = 0; i < expectedLines.length; i++) {
			Assert.assertEquals(expectedLines[i].trim(), foundLines[i].trim());
		}
	}
	
	static String getSourceFoldersTogetherSubjectAntFile() throws IOException{
		return getSubjectFile("subject_build_with_sources_together.template.xml");
	}


	static String getSubjectAntFile() throws IOException {
		return getSubjectFile("subject_build.template.xml");
	}


	private static String getSubjectFile(String fileName) throws IOException {
		final InputStream resourceAsStream = 
			DotClasspathToAntConverterTest.class
				.getResourceAsStream(fileName);
		
		final String classpathWithTwoSrcTwoLibOneOutput = IOUtils.toString(resourceAsStream);
		return classpathWithTwoSrcTwoLibOneOutput;
	}
}
