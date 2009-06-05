package antlips.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;
import antlips.antFileGenerator.AntFileBuilderToFilesystem;
import antlips.antFileGenerator.AntUtils;
import antlips.antFileGenerator.DotClasspathToAntConverter;
import antlips.dotClasspathParser.DotClasspath;

public class AntFileBuilderTest {
	
	private Directory _directory;
	private DotClasspath _classpath;

	@Before
	public void setup(){
		_directory = new TransientDirectory();
		_classpath = new DotClasspath(
			Arrays.asList(DotClasspath.entry("spikes/src"), DotClasspath.entry("wheel/src", "sneerAPI-bin")),
			Arrays.asList("kernel/lib/asm-all-3.1.jar", "bricks/lib/bcprov-jdk16-139.jar"));
	}
	
	@Test
	public void testAntFileBuilder() throws IOException{				
		
		new DotClasspathToAntConverter(_directory).createAntFile(_classpath);
		
		final String subjectAntFileTemplate = getSubjectAntFile();
		final String generated = _directory.contentsAsString("antlips.xml");
		
		assertEqualsIgnoringWhitespace(subjectAntFileTemplate, generated);
	}

	@Test
	public void testAntFileBuilderCompileSourceFoldersTogether() throws IOException{				
		
		new DotClasspathToAntConverter(new AntFileBuilderToFilesystem(_directory, true)).createAntFile(_classpath);
		
		final String subjectAntFileTemplate = getSourceFoldersTogetherSubjectAntFile(); 
		final String generated = _directory.contentsAsString("antlips.xml");
		
		assertEqualsIgnoringWhitespace(subjectAntFileTemplate, generated);
	}
	
	private void assertEqualsIgnoringWhitespace(String expected, String found) {
		Assert.assertEquals(normalizeWhitespace(expected), normalizeWhitespace(found));
	}

	private String normalizeWhitespace(String expected) {
		return expected.trim().replace("\r\n", "\n");
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
		
		final String classpathWithTwoSrcTwoLibOneOutput = AntUtils.readString(resourceAsStream);
		return classpathWithTwoSrcTwoLibOneOutput;
	}
}
