package sneer.kernel.container.impl.classloader.enhancer;

import org.objectweb.asm.ClassVisitor;

public interface Enhancer {

	ClassVisitor enhance(ClassVisitor visitor);

}
