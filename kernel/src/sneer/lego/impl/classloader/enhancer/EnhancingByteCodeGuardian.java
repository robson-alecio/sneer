package sneer.lego.impl.classloader.enhancer;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;


public class EnhancingByteCodeGuardian implements ByteCodeGuardian {

	private Enhancer _enhancer;

	private Map<String, byte[]> _cache;
	
	private static final ByteCodeGuardian INSTANCE = new EnhancingByteCodeGuardian();
	
	private EnhancingByteCodeGuardian() {
		_enhancer = new MakeSerializable();
		_cache = new HashMap<String, byte[]>();
	}
	
	public static final ByteCodeGuardian instance() {
		return INSTANCE;
	}
	
	@Override
	public byte[] enhance(String name, byte[] byteArray) {
		byte[] result = _cache.get(name);
		if(result != null) return result;
		ClassReader reader = new ClassReader(byteArray);
		ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		reader.accept(_enhancer.enhance(writer), 0);
		result = writer.toByteArray(); 
		_cache.put(name, result);
		return result;
	}
}
