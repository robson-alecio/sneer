package sneer.kernel.install;

import static sneer.kernel.SneerDirectories.writeMainAppFile;

import java.io.IOException;

import sneer.kernel.server.Command;
import wheel.io.Log;

public class VersionUpdateCommand implements Command {

	private final int _version;
	private final byte[] _contents;

	public VersionUpdateCommand(int version, byte[] contents) {
		_version = version;
		_contents = contents;
	}

	public void execute() {
		Log.log("Salvando atualização para o Sneer...");

		try {
			writeMainAppFile(_contents, _version);
			Log.log("Atualização salva.");
		} catch (IOException e) {
			Log.log(e);
		}
	}

	private static final long serialVersionUID = 1L;

}
