package sneer.bricks.hardwaresharing.files.server.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardwaresharing.files.protocol.FileContents;
import sneer.bricks.hardwaresharing.files.protocol.FileRequest;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.hardwaresharing.files.server.FileServer;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.lang.Consumer;

public class FileServerImpl implements FileServer, Consumer<FileRequest> {
	
	
	@SuppressWarnings("unused") private final WeakContract _fileRequestContract;
	private final Light _errorLight = my(BlinkingLights.class).prepare(LightType.ERROR);
	
	
	{
		my(TupleSpace.class).keep(FileRequest.class);
		_fileRequestContract = my(TupleSpace.class).addSubscription(FileRequest.class, this);
	}
	
	
	@Override
	public Sneer1024 serve(File fileOrFolder) throws IOException {
		return FolderStructureCache.cache(fileOrFolder);
	}

	
	@Override
	public void consume(FileRequest request) {
		try {
			tryToReply(request);
		} catch (IOException e) {
			my(BlinkingLights.class).turnOnIfNecessary(_errorLight, "Error reading file.", helpMessage(), e);
		}
	}


	private void tryToReply(FileRequest request) throws IOException {
		final Object response = FolderStructureCache.getContents(request.hashOfContents);
		if (response == null) return;
		
		my(TupleSpace.class).publish(asTuple(response));
	}


	private Tuple asTuple(Object response) {
		return response instanceof FolderContents
			? (FolderContents)response
			: asFileContents((byte[])response);
	}


	private FileContents asFileContents(byte[] contents) {
		return new FileContents(my(ImmutableArrays.class).newImmutableByteArray(contents));
	}

	
	private String helpMessage() {
		return "There was trouble reading files from disk. See log for details.";
	}

}