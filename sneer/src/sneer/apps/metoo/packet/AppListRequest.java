package sneer.apps.metoo.packet;

import sneer.apps.metoo.MeTooPacket;

public class AppListRequest implements MeTooPacket{

	public int type() {
		return APP_LIST_REQUEST; 
	}

}
