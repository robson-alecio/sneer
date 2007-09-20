package sneer.apps.metoo.packet;

import sneer.apps.metoo.MeTooPacket;

public class AppListRequest implements MeTooPacket{

	private static final long serialVersionUID = 1L;

	public int type() {
		return APP_LIST_REQUEST; 
	}

}
