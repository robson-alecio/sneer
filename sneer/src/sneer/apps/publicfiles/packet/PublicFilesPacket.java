package sneer.apps.publicfiles.packet;

import java.io.Serializable;

public interface PublicFilesPacket extends Serializable{
	public static final int LIST_OF_FILES = 0;
	public static final int PLEASE_SEND_FILE = 1;
	
	public int type();
}
