package sneer.bricks.software.code.metaclass.asm;

public interface ClassVisitor extends Visitor{

	void visit(int version, int access, String name, String signature, String superName, String[] interfaces);

}
