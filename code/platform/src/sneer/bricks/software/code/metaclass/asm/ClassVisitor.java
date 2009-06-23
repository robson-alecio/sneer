package sneer.bricks.software.code.metaclass.asm;

public interface ClassVisitor {

	void visit(int version, int access, String name, String signature, String superName, String[] interfaces);

}
