package sneer.apps.filetransfer;

import static wheel.i18n.Language.translate;
import sneer.apps.asker.packet.AskerRequestPayload;

public class FileRequest extends AskerRequestPayload{

	public final String _filename;
	public final long _size;

	public FileRequest(String filename, long size){
		_filename = filename;
		_size = size;
	}
	
	@Override
	public String prompt() {
		return translate("Do you accept the file %1$s ?",_filename);
	}

	private static final long serialVersionUID = 1L;
}
