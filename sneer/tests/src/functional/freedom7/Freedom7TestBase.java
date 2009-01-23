package functional.freedom7;

import static wheel.lang.Environments.my;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import sneer.pulp.compiler.JavaCompiler;
import wheel.io.SourceFileWriter;
import functional.SovereignFunctionalTestBase;
import functional.SovereignParty;

public abstract class Freedom7TestBase extends SovereignFunctionalTestBase {
	
	private final JavaCompiler _compiler = my(JavaCompiler.class);
	
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
		
		fail("The last line in BrickFileImpl must be fixed.");
	}

	@Test
	public void testPublishBrickWithDependencies() throws Exception {
		System.clearProperty("freedom7.z.Z.installed");
		publisher().publishBricks(generateY());
		publisher().publishBricks(generateZ());
		assertEquals("true", System.getProperty("freedom7.z.Z.installed"));
	}
	
	@Test
	public void testPublishBrickWithLib() throws Exception {
		System.clearProperty("freedom7.lib.Lib.executed");
		publisher().publishBricks(generateX());
		assertEquals("true", System.getProperty("freedom7.lib.Lib.executed"));
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
	
	private File generateX() throws IOException {
		generateLib(sourceFolder("x/freedom7/x/impl/lib/lib.jar"));
		
		final File src = sourceFolder("x");
		final SourceFileWriter writer = new SourceFileWriter(src);
		writer.write("freedom7.x.X",
				"public interface X extends sneer.kernel.container.Brick {" +
				"}");
		writer.write("freedom7.x.impl.XImpl",
				"class XImpl implements freedom7.x.X {\n" +
					"public XImpl() {\n" +
						"freedom7.lib.Lib.execute();\n" +
					"}" +
				"}");	
		return src;
	}

	private void generateLib(final File libJar) throws IOException {
		final File lib = sourceFolder("lib");
		new SourceFileWriter(lib).write("freedom7.lib.Lib",
			"public class Lib {" +
				"public static void execute() {" +
					"System.setProperty(\"freedom7.lib.Lib.executed\", \"true\");" +
				"}" +
			"}");
		
		new LibBuilder(_compiler, lib, sourceFolder("bin")).build(libJar);
	}

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
				"import static wheel.lang.Environments.my;\n" +
				"class ZImpl implements freedom7.z.Z {\n" +
					"private freedom7.y.Y _y = my(freedom7.y.Y.class);\n" +
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