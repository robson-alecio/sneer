package sneer.apps.metoo.packet;

import sneer.apps.metoo.MeTooPacket;

public class AppInstallRequest implements MeTooPacket{

	public final String _installName;
	
	public AppInstallRequest(String installName){
		_installName = installName;
		
	}
	
	public int type() {
		return APP_INSTALL_REQUEST;
	}

}
