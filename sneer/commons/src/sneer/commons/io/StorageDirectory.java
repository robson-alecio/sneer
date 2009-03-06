package sneer.commons.io;

/** Used to configure where code running in a certain environment should persist its data. */
public interface StorageDirectory {

	String getPath();

}
