package sneer.bricks.hardwaresharing.files.protocol;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Tuple;

public class FileOrFolder extends Tuple {

	public final String name;
	public final long lastModified;
	public final Sneer1024 hashOfContents;

	public FileOrFolder(String name_, long lastModified_, Sneer1024 hashOfContents_) {
		name = name_;
		lastModified = lastModified_;
		hashOfContents = hashOfContents_;
	}

}
