package sneer.kernel.container.impl.classloader.enhancer;

import org.objectweb.asm.*;

public interface Enhancer {

	ClassVisitor enhance(ClassVisitor visitor);

}
