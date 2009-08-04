package sneer.bricks.hardwaresharing.files.protocol;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Tuple;

public class FileRequest extends Tuple {

	public final Sneer1024 hashOfContents;

	public FileRequest(Sneer1024 hashOfContents_) {
		hashOfContents = hashOfContents_;
	}
	
	@Override
	public String toString() {
		return "FileRequest: " + hashOfContents;
	}
	
}
