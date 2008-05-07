package sneer.lego.impl.classloader.enhancer;

public interface ByteCodeGuardian {
	
	byte[] enhance(String name, byte[] byteArray);
}
