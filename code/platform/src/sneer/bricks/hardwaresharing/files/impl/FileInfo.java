package sneer.bricks.hardwaresharing.files.impl;

import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.foundation.brickness.Tuple;

class FileInfo extends Tuple {

	public final String name;
	public final boolean isDirectory;
	public final long lastModified;
	public final ImmutableByteArray hashOfContents;

	public FileInfo(String name_, boolean isDirectory_, long lastModified_, ImmutableByteArray hashOfContents_) {
		name = name_;
		isDirectory = isDirectory_;
		lastModified = lastModified_;
		hashOfContents = hashOfContents_;
	}

}
