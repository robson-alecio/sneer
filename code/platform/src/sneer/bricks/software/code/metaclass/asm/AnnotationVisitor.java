package sneer.bricks.software.code.metaclass.asm;

public interface AnnotationVisitor extends Visitor{

	void visit(String typeSignature, boolean visible, AnnotationInfo annotationInfoImpl);
}
