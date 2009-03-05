package sneer.kernel.container.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sneer.brickness.environments.Conventions;
import wheel.io.SourceFileWriter;

class ImplementationGenerator {

	static String generateFor(Class<?> intrface) {
		return new ImplementationGenerator(intrface)._resultMessage;
	}
	
	private ImplementationGenerator(Class<?> intrface) {
		_interface = intrface;
		
		String resultMessage;
		try {
			generateImpl();
			resultMessage = successMessage(intrface);
		} catch (Exception e) {
			resultMessage = failureMessage(intrface, e);
		}
		_resultMessage = resultMessage;
	}

	private final Class<?> _interface;
	private final String _resultMessage;

	private void generateImpl() throws IOException {
		sourceWriter().write(implClassName(), implCode());
	}

	private String implCode() {
		String intrface = _interface.getSimpleName();
		return
			"\nimport " + _interface.getName() + ";\n\n" +
			"class " + intrface+"Impl" + " implements " + intrface + " {\n\n" +
			"}";

	}

	private String implClassName() {
		return Conventions.implementationNameFor(_interface.getName());
	}

	private SourceFileWriter sourceWriter() {
		String interfaceName = _interface.getName();

		for (File srcFolder : srcFolders()) {
			SourceFileWriter candidate = new SourceFileWriter(srcFolder);
			if (candidate.javaFile(interfaceName).exists()) return candidate;
		}
		throw new RuntimeException("Unable to find src folder containing: " + interfaceName);
	}

	private Iterable<File> srcFolders() {
		ArrayList<File> result = new ArrayList<File>();

		collectSrcFolders(new File("."), result);
		
		return result;
	}

	private void collectSrcFolders(File candidate, ArrayList<File> result) {
		if (!candidate.isDirectory()) return;
		if (candidate.getName().equals("src")) result.add(candidate);
		
		for (File file : candidate.listFiles())
			collectSrcFolders(file, result);
	}

	private String successMessage(Class<?> intrface) {
		return " >>> Successfully Generated Impl for: " + intrface.getName() + " (Refresh your IDE).";
	}

	private String failureMessage(Class<?> intrface, Exception e) {
		return " >>> Unable to generate Impl for " + intrface.getName() + ": " + e.getMessage();
	}
	

}
