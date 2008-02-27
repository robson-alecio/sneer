package wheel.io.serialization.tests;

import wheel.io.serialization.OptimizedDeserializer;
import wheel.io.serialization.OptimizedSerializer;
import junit.framework.TestCase;

public class OptimizedSerializationTest extends TestCase {

	private OptimizedSerializer _serializer;
	private static OptimizedDeserializer _deserializer;

	public void testOptimizedSerialization() throws Exception {
		_serializer = new OptimizedSerializer(3);
		_deserializer = new OptimizedDeserializer(this.getClass().getClassLoader());
		
		process("Foo");
		process("Bar");
		process(123);
		process("Banana");
		process("Apple");
		process(4321);
		
	}
	
	private void process(Object object) throws Exception {
		byte[] bytes = _serializer.serialize(object);
		Object copy = _deserializer.deserialize(bytes);
		assertEquals(object, copy);
	}
	
}
