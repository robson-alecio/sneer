package spikes.wheel.io.tests;

import sneer.brickness.testsupport.TestThatMightUseResources;

public class JarBuilderTest extends TestThatMightUseResources {

//	@Test
//	public void testSimpleJar() throws Exception {
//		// test data
//		String content = "sample content\nnew line";
//		File data = File.createTempFile("entry-", ".txt", tmpDirectory());
//		FileUtils.writeStringToFile(data, content);
//		
//		//create jar file
//		File file = File.createTempFile("myJar-", ".jar", tmpDirectory());
//		JarBuilder jar = new JarBuilder(file);
//		try {
//			jar.add("entry.txt", data);
//		} finally {
//			Streams.crash(jar);
//		}
//		
//		//test
//		JarFile jarFile = new JarFile(file);
//		try {
//			InputStream is = jarFile.getInputStream(jarFile.getEntry("entry.txt"));
//			assertEquals(content, read(is));
//		} finally {
//			jarFile.close();
//		}
//	}
//
//	private String read(InputStream is) throws IOException {
//		StringWriter writer = new StringWriter();
//		IOUtils.copy(is, writer);
//		return writer.getBuffer().toString();
//	}
}