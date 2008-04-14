package sneer.lego.impl.classloader;

import org.objectweb.asm.*;

public interface Enhancer {

	ClassVisitor enhance(ClassVisitor visitor);

}
