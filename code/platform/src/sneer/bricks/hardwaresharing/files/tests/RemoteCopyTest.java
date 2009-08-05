package sneer.bricks.hardwaresharing.files.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardwaresharing.files.client.FileClient;
import sneer.bricks.hardwaresharing.files.server.FileServer;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.Closure;

public class RemoteCopyTest extends LocalCopyTest {

	@Override
	protected void copyFromFileCache(final Sneer1024 hashOfContents, final File destination) throws IOException {
		@SuppressWarnings("unused")
		FileServer server = my(FileServer.class);
		
		Environment remote = newTestEnvironment(my(TupleSpace.class), my(OwnNameKeeper.class));
		Environments.runWith(remote, new Closure<IOException>() { @Override public void run() throws IOException {
			fetch(hashOfContents, destination);
		}});
	}

	private void fetch(Sneer1024 hashOfContents, File destination) throws IOException {
		my(FileClient.class).fetchToCache(hashOfContents);
		super.copyFromFileCache(hashOfContents, destination);
	}

}
