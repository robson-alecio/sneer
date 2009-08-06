package sneer.bricks.hardwaresharing.files.protocol;

import sneer.bricks.hardware.ram.arrays.ImmutableArray;
import sneer.foundation.brickness.Tuple;

public class FolderContents extends Tuple {

	public final ImmutableArray<FileOrFolder> contents;

	public FolderContents(ImmutableArray<FileOrFolder> contents_) {
		contents = contents_;
	}

}
