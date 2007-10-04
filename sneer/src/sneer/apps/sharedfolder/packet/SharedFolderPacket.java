package sneer.apps.sharedfolder.packet;

import java.io.Serializable;

public interface SharedFolderPacket extends Serializable{
	public static final int LIST_OF_FILES = 0;
	public static final int PLEASE_SEND_FILE = 1;
	public static final int SENDING_FILE = 2;
	
	public int type();
}
