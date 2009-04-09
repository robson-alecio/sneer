package sneer.pulp.natures.gui.impl;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import sneer.brickness.ClassDefinition;
import sneer.pulp.natures.gui.GUI;

class GUIImpl implements GUI {

	@Override
	public List<ClassDefinition> realize(final ClassDefinition classDef) {
		
		final ArrayList<ClassDefinition> result = new ArrayList<ClassDefinition>();
		
		final ClassReader source = new ClassReader(classDef.bytes());
		final ClassWriter target = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		final ClassAdapter transformer = new ClassAdapter(target) {
			
			@Override
			public MethodVisitor visitMethod(int access, final String methodName,
					final String desc, String signature, String[] exceptionTypes) {
				
				final MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptionTypes);
				if (methodName.equals("<init>")) 
					return mv;
				
				return new GUIMethodEnhancer(mv, access, methodName, desc, classDef, result);
			}
			
		};
		source.accept(transformer, ClassReader.EXPAND_FRAMES);
		result.add(new ClassDefinition(classDef.name(), target.toByteArray()));
		return result;
	}

	

}
