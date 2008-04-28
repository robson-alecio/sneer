package sneer.lego.impl.classloader.enhancer;

import java.io.IOException;

import sneer.lego.utils.metaclass.MetaClass;

public interface ByteCodeGuardian {
	
	@Deprecated
	byte[] enhance(String name, byte[] byteArray);

	byte[] enhance(MetaClass meta) throws IOException;
}
