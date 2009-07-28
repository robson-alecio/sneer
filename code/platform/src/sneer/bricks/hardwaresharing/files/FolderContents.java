package sneer.bricks.hardwaresharing.files;

import sneer.bricks.hardware.ram.arrays.ImmutableArray;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Tuple;

public class FolderContents extends Tuple {

	public final ImmutableArray<Sneer1024> contents;

	public FolderContents(ImmutableArray<Sneer1024> contents_) {
		contents = contents_;
	}

}
