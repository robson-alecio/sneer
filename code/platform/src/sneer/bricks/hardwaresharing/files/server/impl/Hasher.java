package sneer.bricks.hardwaresharing.files.server.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import sneer.bricks.hardwaresharing.files.server.FolderContents;
import sneer.bricks.hardwaresharing.files.server.FolderEntry;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Digester;
import sneer.bricks.pulp.crypto.Sneer1024;

class Hasher {

	static Sneer1024 hashFileContents(File file) throws IOException {
		return my(Crypto.class).digest(file);
	}
	
	static Sneer1024 hashFolderContents(FolderContents folder) {
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
