package sneer.bricks.software.bricks.finder.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.EmptyVisitor;

import sneer.bricks.software.bricks.finder.BrickFinder;
import sneer.foundation.brickness.Brick;

public class BrickFinderImpl implements BrickFinder {

	private static final String BRICK_TYPE_SIGNATURE = "L" + Brick.class.getName().replace('.', '/')+ ";";

	@Override
	public Collection<String> findBricks(File binDirectory) throws IOException {
		Collection<String> result = new ArrayList<String>();
		
		for (File candidate : findClassFileCandidates(binDirectory)) {
			ClassVisitor visitor = new ClassVisitor(candidate);
			if (visitor._foundBrick)
				result.add(visitor._className);
		}
		
		return result;
	}

	private Collection<File> findClassFileCandidates(File binDirectory) {
		IOFileFilter dirFilter = FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("impl"));
		IOFileFilter fileFilter = FileFilterUtils.suffixFileFilter(".class");
		return FileUtils.listFiles(binDirectory, fileFilter, dirFilter);
	}

	private class ClassVisitor extends EmptyVisitor {

		boolean _foundBrick = false;
		String _className;

		public ClassVisitor(File classFile) throws IOException {
			new ClassReader(new FileInputStream(classFile)).accept(this, 0);
		}

		@Override
		public AnnotationVisitor visitAnnotation(String typeSignature, boolean visible) {
			if (BRICK_TYPE_SIGNATURE.equals(typeSignature))
				_foundBrick = true;
			return super.visitAnnotation(typeSignature, visible);
		}

		@Override
		public void visit(int version, int access, String className, String signature, String superName, String[] interfaces) {
			_className = className.replace('/', '.');
			super.visit(version, access, className, signature, superName, interfaces);
		}

	}

	
}
