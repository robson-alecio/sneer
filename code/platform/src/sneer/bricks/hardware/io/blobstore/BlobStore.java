package sneer.bricks.hardware.io.blobstore;


public interface BlobStore {

	void writeObjectFor(Class<?> brick, Object object);
	Object readObjectFor(Class<?> brick);
	
}
