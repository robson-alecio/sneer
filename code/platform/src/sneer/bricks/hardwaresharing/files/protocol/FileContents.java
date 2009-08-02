package sneer.bricks.hardwaresharing.files.protocol;

import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.foundation.brickness.Tuple;

public class FileContents extends Tuple {

	public final ImmutableByteArray bytes;

	public FileContents(ImmutableByteArray bytes_) {
		bytes = bytes_;
	}

}
