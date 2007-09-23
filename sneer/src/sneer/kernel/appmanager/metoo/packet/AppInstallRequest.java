package sneer.kernel.appmanager.metoo.packet;

import sneer.kernel.appmanager.metoo.MeTooPacket;

public class AppInstallRequest implements MeTooPacket {

	private static final long serialVersionUID = 1L;

	public final String _installName;
	
	public AppInstallRequest(String installName){
		_installName = installName;
		
	}
	
	public int type() {
		return APP_INSTALL_REQUEST;
	}

}
