package spikes.sneer.kernel.container.impl.classloader.enhancer;

public interface ByteCodeGuardian {
	
	byte[] enhance(String name, byte[] byteArray);
}
