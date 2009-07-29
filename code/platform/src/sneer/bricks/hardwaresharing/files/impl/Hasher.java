package sneer.bricks.hardwaresharing.files.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import sneer.bricks.hardwaresharing.files.FileContents;
import sneer.bricks.hardwaresharing.files.FolderContents;
import sneer.bricks.hardwaresharing.files.FolderEntry;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Digester;
import sneer.bricks.pulp.crypto.Sneer1024;

class Hasher {

	static Sneer1024 hashFile(FileContents block) {
		return my(Crypto.class).digest(block.bytes.copy());
	};
	
	static Sneer1024 hashFolder(FolderContents folder) {
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
