package metoo.packet;

import metoo.MeTooPacket;

public class AppListRequest implements MeTooPacket{

	public int type() {
		return APP_LIST_REQUEST;
	}

}
