package sneer.bricks.hardwaresharing.files.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.bricks.hardwaresharing.files.DataBlock;
import sneer.bricks.hardwaresharing.files.FileSpace;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Digester;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.NotImplementedYet;

public class FileSpaceImpl implements FileSpace {

	private static final byte[] TRUE_AS_BYTES = new byte[]{1};
	private static final byte[] FALSE_AS_BYTES = new byte[]{0};
	
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
		return (fileOrDirectory.isDirectory())
			? publishDirectoryContents(fileOrDirectory)
			: publishFileContents(fileOrDirectory);
	}

	private Sneer1024 publishFileContents(File file)	throws IOException {
		my(TupleSpace.class).publish(newDataBlock(file));
		return my(Crypto.class).digest(file); //Optimize byte[] is being read already.
	}
	
	private Sneer1024 publishDirectoryContents(File directory) throws IOException {
		@SuppressWarnings("unused")
		List<Sneer1024> files = publishEachFile(directory);
		
		
		//	meta dir: hashes de todos os meta arquivos (blocos de hashes recursivos)
		//		meta arquivo: fileinfo (hash conteudo)
		//			conteudo
		
		throw new NotImplementedYet();
	}

	private List<Sneer1024> publishEachFile(File directory) throws IOException {
		List<Sneer1024> result = new ArrayList<Sneer1024>();
		for (File fileOrDirectory : directory.listFiles()) {
			Sneer1024 hashOfContents = publishContents(fileOrDirectory);
			
			FileInfo fileInfo = fileInfoFor(fileOrDirectory, hashOfContents);
			my(TupleSpace.class).publish(fileInfo);
						
			result.add(hash(fileInfo));
		}
		return result;
	}

	private Sneer1024 hash(FileInfo fileInfo) {
		Digester digester = my(Crypto.class).newDigester();
		digester.update(bytesUtf8(fileInfo.name));
		digester.update(fileInfo.isDirectory ? TRUE_AS_BYTES : FALSE_AS_BYTES);
		digester.update(BigInteger.valueOf(fileInfo.lastModified).toByteArray());
		digester.update(fileInfo.hashOfContents.copy());
		return digester.digest();
	}

	private byte[] bytesUtf8(String string) {
		try {
			return string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	private FileInfo fileInfoFor(File fileOrDirectory, Sneer1024 hashOfContents) {
		return new FileInfo(
			fileOrDirectory.getName(),
			fileOrDirectory.isDirectory(),
			fileOrDirectory.lastModified(),
			my(ImmutableArrays.class).newImmutableByteArray(hashOfContents.bytes())
		);
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
