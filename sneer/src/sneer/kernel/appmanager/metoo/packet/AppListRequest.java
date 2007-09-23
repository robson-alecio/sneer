package sneer.kernel.appmanager.metoo.packet;

import sneer.kernel.appmanager.metoo.MeTooPacket;

public class AppListRequest implements MeTooPacket{

	private static final long serialVersionUID = 1L;

	public int type() {
		return APP_LIST_REQUEST; 
	}

}
