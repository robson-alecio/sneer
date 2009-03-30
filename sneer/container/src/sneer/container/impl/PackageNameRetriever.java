package sneer.container.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.EmptyVisitor;

import sneer.commons.lang.ByRef;

class PackageNameRetriever {

	private String _classDirectory;

	PackageNameRetriever(String classDirectory) {
		_classDirectory = classDirectory;
	}

	String retrieve() throws IOException {
		return packageNameFor(ClassFiles.list(_classDirectory)[0]);
	}

	private String packageNameFor(File file) throws IOException {
		final ByRef<String> packageName = ByRef.newInstance();
		ClassReader reader = new ClassReader(IOUtils.toByteArray(new FileInputStream(file)));
		reader.accept(new EmptyVisitor() {
			@Override
			public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
				packageName.value = name.substring(0, name.lastIndexOf('/')).replace('/', '.');
			}
		}, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
		return packageName.value;
	}
}
