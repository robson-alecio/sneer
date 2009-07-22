package sneer.bricks.hardwaresharing.files;

import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.foundation.brickness.Tuple;

public class DataBlock extends Tuple {

	public final ImmutableByteArray bytes;

	public DataBlock(ImmutableByteArray bytes_) {
		bytes = bytes_;
	}

}
