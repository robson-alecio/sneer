package sneer.bricks.hardwaresharing.files.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.bricks.hardwaresharing.files.DataBlock;
import sneer.bricks.hardwaresharing.files.FileSpace;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.NotImplementedYet;

public class FileSpaceImpl implements FileSpace {

	private Consumer<DataBlock> _refToAvoidGc;
	private Map<Sneer1024, ImmutableByteArray> _blocksByHash = new ConcurrentHashMap<Sneer1024, ImmutableByteArray>();

	{
		my(TupleSpace.class).keep(DataBlock.class);
		
		_refToAvoidGc = new Consumer<DataBlock>() { @Override public void consume(DataBlock block) {
			_blocksByHash.put(hash(block), block.bytes);
		}};
		my(TupleSpace.class).addSubscription(DataBlock.class, _refToAvoidGc);
	}
	
	@Override
	public Sneer1024 publishContents(File fileOrDirectory) throws IOException {
		if (fileOrDirectory.isDirectory()) throw new NotImplementedYet();
		my(TupleSpace.class).publish(newDataBlock(fileOrDirectory));
		return my(Crypto.class).digest(fileOrDirectory); //Optimize byte[] is being read already.
	}

	@Override
	public void fetchContentsInto(File destination, Sneer1024 hash) throws IOException {
		byte[] contents = _blocksByHash.get(hash).copy();
		my(IO.class).files().writeByteArrayToFile(destination, contents);
	}
	
	private DataBlock newDataBlock(File fileOrDirectory) throws IOException {
		byte[] bytes = my(IO.class).files().readBytes(fileOrDirectory);
		return new DataBlock(my(ImmutableArrays.class).newImmutableByteArray(bytes));
	}

	private Sneer1024 hash(DataBlock block) {
		return my(Crypto.class).digest(block.bytes.copy());
	};

}
