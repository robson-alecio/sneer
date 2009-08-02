package sneer.bricks.hardwaresharing.files.hasher.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import sneer.bricks.hardwaresharing.files.hasher.Hasher;
import sneer.bricks.hardwaresharing.files.protocol.FileContents;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.hardwaresharing.files.protocol.FolderEntry;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Digester;
import sneer.bricks.pulp.crypto.Sneer1024;

class HasherImpl implements Hasher {

	@Override
	public Sneer1024 hashFile(FileContents contents) {
		return hashFile(contents.bytes.copy());
	}

	@Override
	public Sneer1024 hashFile(byte[] contents) {
		return my(Crypto.class).digest(contents);
	}
	
	@Override
	public Sneer1024 hashFolder(FolderContents folder) {
		Digester digester = my(Crypto.class).newDigester();
		for (FolderEntry entry : folder.contents)
			digester.update(hash(entry).bytes());
		return digester.digest();
	}

	private static Sneer1024 hash(FolderEntry entry) {
		Digester digester = my(Crypto.class).newDigester();
		digester.update(bytesUtf8(entry.name));
		digester.update(BigInteger.valueOf(entry.lastModified).toByteArray());
		digester.update(entry.hashOfContents.bytes());
		return digester.digest();
	}

	private static byte[] bytesUtf8(String string) {
		try {
			return string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

}
