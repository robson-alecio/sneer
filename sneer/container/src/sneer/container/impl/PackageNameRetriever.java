package sneer.container.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.EmptyVisitor;

import sneer.commons.lang.ByRef;

class PackageNameRetriever {

	static String packageNameFor(String classDirectory) throws IOException {
		return packageNameFor(ClassFiles.list(classDirectory)[0]);
	}

	static private String packageNameFor(File file) throws IOException {
		final ByRef<String> result = ByRef.newInstance();
		ClassReader reader = new ClassReader(IOUtils.toByteArray(new FileInputStream(file)));
		reader.accept(new EmptyVisitor() { @Override public void visit(int versionIgnored, int accessIgnored, String className, String signatureIgnored, String superNameIgnored, String[] interfacesIgnored) {
			
			result.value = className.substring(0, className.lastIndexOf('/')).replace('/', '.');
			
		}}, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
		return result.value;
	}

}
