package sneer.lego.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import sneer.lego.utils.JarBuilder;

public class JarBuilderTest {

	@Test
	public void testBuildJarFile() throws Exception {

		//test data
		String content = "sample content\nnew line";
		File data = File.createTempFile("entry-", ".txt");
		FileUtils.writeStringToFile(data, content);
		
		//create jar file
		JarBuilder builder = JarBuilder.builder("/tmp/myJar.jar");
		builder.add("entry.txt", data);
		File result = builder.close();
		
		//test
		JarFile jarFile = new JarFile(result);
		InputStream is = jarFile.getInputStream(jarFile.getEntry("entry.txt"));
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer);
		assertEquals(content, writer.getBuffer().toString());
	}
}
