package sneer.kernel.container.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import sneer.kernel.container.jar.DeploymentJar;
import sneer.kernel.container.jar.impl.DeploymentJarImpl;
import wheel.io.Streams;

public class DeploymentJarTest {

	@Test
	public void testBuildJarFile() throws Exception {

		//test data
		String content = "sample content\nnew line";
		File data = File.createTempFile("entry-", ".txt");
		FileUtils.writeStringToFile(data, content);
		
		//create jar file
		File file = File.createTempFile("myJar-", ".jar");
		DeploymentJar jar = new DeploymentJarImpl(file);
		jar.add("entry.txt", data);
		Streams.crash(jar);
		
		//test
		InputStream is = jar.getInputStream("entry.txt");
		assertEquals(content, read(is));

		//test
		JarFile jarFile = new JarFile(jar.file());
		is = jarFile.getInputStream(jarFile.getEntry("entry.txt"));
		assertEquals(content, read(is));
	}

	private String read(InputStream is) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer);
		return writer.getBuffer().toString();
	}
}
