package functional.freedom7;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import functional.SovereignFunctionalTest;
import functional.SovereignParty;

public abstract class Freedom7Test extends SovereignFunctionalTest {
	
	@Test
	public void testPublishSingleBrick() throws Exception {
		System.clearProperty("freedom7.y.Y.installed");
		publisher().publishBricks(generateY());
		assertEquals("true", System.getProperty("freedom7.y.Y.installed"));
	}
	
	@Test
	public void testPublishTwoBricks() throws Exception {
		System.clearProperty("freedom7.y.Y.installed");
		System.clearProperty("freedom7.z.Z.installed");
		publisher().publishBricks(generateYandZ());
		assertEquals("true", System.getProperty("freedom7.y.Y.installed"));
		assertEquals("true", System.getProperty("freedom7.z.Z.installed"));
	}

	@Test
	public void testPublishBrickWithDependencies() throws Exception {
		System.clearProperty("freedom7.z.Z.installed");
		publisher().publishBricks(generateY());
		publisher().publishBricks(generateZ());
		assertEquals("true", System.getProperty("freedom7.z.Z.installed"));
	}
		
	@Ignore
	@Test
	public void testMeTooSingleBrick() throws Exception {
//		publishY();
//		publishXandZ();
//		receiver().meToo(publisher(), Z.class.getName());
//		assertBrickInstallation(Z.class, receiver());
//	
//		Z z1 = (Z) publisher().produce(Z.class);
//		ClassLoader libClassLoader1 = z1.libClassLoader();
//	
//		Z z2 = (Z) receiver().produce(Z.class);
//		ClassLoader libClassLoader2 = z2.libClassLoader();
//		
//		assertNotSame("LogFactory should have been loaded on a different class loader", libClassLoader1, libClassLoader2);
//		assertNotSame(z1, z2);
//		assertNotSame(classLoaderFor(z1), classLoaderFor(z2));
	}
	
//	private SovereignParty receiver() {
//		return _b;
//	}
	
//	private ClassLoader classLoaderFor(Object z2) {
//		return z2.getClass().getClassLoader();
//	}
	
	private File generateYandZ() throws IOException {
		final File src = sourceFolder("src-yz");
		final SourceFileWriter writer = new SourceFileWriter(src);
		writeY(writer);
		writeZ(writer);
		return src;
	}

	private File generateY() throws IOException {
		File src = sourceFolder("src-y");
		writeY(new SourceFileWriter(src));
		return src;
	}

	private void writeY(SourceFileWriter writer) throws IOException {
		writer.write("freedom7.y.Y", "public interface Y extends sneer.kernel.container.Brick {}");
		writer.write("freedom7.y.impl.YImpl",
				"class YImpl implements freedom7.y.Y {\n" +
					"public YImpl() {\n" +
						"System.setProperty(\"freedom7.y.Y.installed\", \"true\");\n" +
					"}" +
				"}");
	}

	private File generateZ() throws IOException {
		final File src = sourceFolder("src-z");
		writeZ(new SourceFileWriter(src));
		return src;
	}

	private void writeZ(final SourceFileWriter writer) throws IOException {
		writer.write("freedom7.z.Z",
				"public interface Z extends sneer.kernel.container.Brick {\n" +
					"freedom7.y.Y y();\n" +
				"}");
		writer.write("freedom7.z.impl.ZImpl",
				"import sneer.kernel.container.*;\n" +
				"class ZImpl implements freedom7.z.Z {\n" +
					"@Inject\n" +
					"private static freedom7.y.Y _y;\n" +
					"public ZImpl() {\n" +
						"if (_y == null) throw new IllegalStateException();\n" +
						"System.setProperty(\"freedom7.z.Z.installed\", \"true\");\n" +
					"}" +
					"public freedom7.y.Y y() {\n" +
						"return _y;\n" +
					"}\n" +
				"}");
	}


	private SovereignParty publisher() {
		return _a;
	}
	
	private File sourceFolder(String sourceFolder) {
		return new File(tmpDirectory(), sourceFolder);
	}

}