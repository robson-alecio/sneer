package sneer.pulp.serialization;


public interface Serializer {

	byte[] serialize(Object object);

	Object deserialize(byte[] packetReceived, ClassLoader classloader);

}
