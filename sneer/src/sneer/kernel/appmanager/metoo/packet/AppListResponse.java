package sneer.kernel.appmanager.metoo.packet;

import java.util.Map;

import sneer.kernel.appmanager.metoo.MeTooPacket;


public class AppListResponse implements MeTooPacket {

	private static final long serialVersionUID = 1L;

	public final Map<String, String> _installNameAndAppUID;
	
	public AppListResponse(Map<String,String> installNameAndAppUID){
		_installNameAndAppUID = installNameAndAppUID;
	}
	public int type() {
		return APP_LIST_RESPONSE; 
	}

}
