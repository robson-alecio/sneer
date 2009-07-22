package sneer.bricks.software.bricks.finder.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.io.IO.FileFilters;
import sneer.bricks.hardware.io.IO.Filter;
import sneer.bricks.software.bricks.finder.BrickFinder;
import sneer.bricks.software.code.metaclass.asm.ASM;
import sneer.bricks.software.code.metaclass.asm.AnnotationInfo;
import sneer.bricks.software.code.metaclass.asm.AnnotationVisitor;
import sneer.bricks.software.code.metaclass.asm.ClassVisitor;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.foundation.brickness.Brick;

public class BrickFinderImpl implements BrickFinder {

	private static final String BRICK_TYPE_SIGNATURE = "L" + Brick.class.getName().replace('.', '/')+ ";";

	@Override
	public Collection<String> findBricks() {
		Collection<String> unique = new HashSet<String>();
		
		collectBricks(unique, my(DirectoryConfig.class).ownBinDirectory().get());
		collectBricks(unique, my(DirectoryConfig.class).platformBinDirectory().get());

		return sorted(unique);
	}

	private Collection<String> sorted(Collection<String> unique) {
		ArrayList<String> result = new ArrayList<String>(unique);
		Collections.sort(result);
		return result;
	}

	private void collectBricks(Collection<String> result, File binDirectory) {
		for (File candidate : findClassFileCandidates(binDirectory)) {
			Visitor visitor = new Visitor(candidate);
			if (visitor._foundBrick)
				result.add(visitor._className);
		}
	}

	private Collection<File> findClassFileCandidates(File binDirectory) {
		FileFilters filters = my(IO.class).fileFilters();
		
		Filter dirFilter = filters.not(filters.name("impl"));
		Filter fileFilter = filters.suffix(".class");
		return filters.listFiles(binDirectory, fileFilter, dirFilter);
	}

	private class Visitor implements ClassVisitor, AnnotationVisitor{

		boolean _foundBrick = false;
		String _className;

		public Visitor(File classFile){
			my(ASM.class).newClassReader(classFile).accept(this);
		}

		@Override
		public void visit(String typeSignature, boolean visible, AnnotationInfo annotationInfo) {
			if (BRICK_TYPE_SIGNATURE.equals(typeSignature))
				_foundBrick = true;
		}

		@Override
		public void visit(int version, int access, String className, String signature, String superName, String[] interfaces) {
			_className = className.replace('/', '.');
		}
	}
}