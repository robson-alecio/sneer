package sneer.bricks.software.bricks.finder.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.EmptyVisitor;

import sneer.bricks.hardware.cpu.exceptions.ExceptionCapsule;
import sneer.bricks.hardware.ram.collections.Collections;
import sneer.bricks.software.bricks.finder.BrickFinder;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.Predicate;

public class BrickFinderImpl implements BrickFinder {

	private static final String BRICK_TYPE_SIGNATURE = "L" + Brick.class.getName().replace('.', '/')+ ";";

	@Override
	public Collection<String> findBricks(File binDirectory) throws IOException {
		try {
			return tryToFindBricks(binDirectory);
		} catch (ExceptionCapsule e) {
			throw (IOException)e.getCause();
		}
	}

	private Collection<String> tryToFindBricks(File binDirectory) {
		IOFileFilter fileFilter = FileFilterUtils.suffixFileFilter(".class");
		IOFileFilter dirFilter = FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("impl"));
		Collection<File> classFiles = FileUtils.listFiles(binDirectory, fileFilter, dirFilter);

		Collection<ClassVisitor> candidates = my(Collections.class).map(classFiles, new Functor<File, ClassVisitor>() { @Override public ClassVisitor evaluate(File brickFile) {
			return new ClassVisitor(brickFile);
		}});
		
		Collection<ClassVisitor> chosen = my(Collections.class).filter(candidates, new Predicate<ClassVisitor>() { @Override public boolean evaluate(ClassVisitor candidate) {
			return candidate._isBrick; 
		}});
		
		return my(Collections.class).map(chosen, new Functor<ClassVisitor, String>() { @Override public String evaluate(ClassVisitor visitor) {
			return visitor._className;
		}});
	}

	private class ClassVisitor extends EmptyVisitor {

		boolean _isBrick = false;
		String _className;

		public ClassVisitor(File classFile) {
			ClassReader classReader = null;
			try {
				classReader = new ClassReader(new FileInputStream(classFile));
			} catch (IOException e) {
				throw new ExceptionCapsule(e);
			}
			
			classReader.accept(this, 0);
		}

		@Override
		public AnnotationVisitor visitAnnotation(String typeSignature, boolean visible) {
			if (BRICK_TYPE_SIGNATURE.equals(typeSignature))
				_isBrick = true;
			return super.visitAnnotation(typeSignature, visible);
		}
	
	}

	
}
