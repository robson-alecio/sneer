package sneer.bricks.software.code.metaclass.asm.impl;

import sneer.bricks.software.code.metaclass.asm.AnnotationInfo;

class AnnotationInfoImpl implements AnnotationInfo{

	@SuppressWarnings("unused")
	private final org.objectweb.asm.AnnotationVisitor _result;

	public AnnotationInfoImpl(org.objectweb.asm.AnnotationVisitor result) {
		_result = result;
	}
	
}