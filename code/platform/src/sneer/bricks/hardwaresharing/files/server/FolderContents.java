package sneer.bricks.hardwaresharing.files.server;

import sneer.bricks.hardware.ram.arrays.ImmutableArray;
import sneer.foundation.brickness.Tuple;

public class FolderContents extends Tuple {

	public final ImmutableArray<FolderEntry> contents;

	public FolderContents(ImmutableArray<FolderEntry> contents_) {
		contents = contents_;
	}

}
