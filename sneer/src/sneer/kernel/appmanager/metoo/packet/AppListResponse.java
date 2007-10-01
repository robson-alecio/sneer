package sneer.kernel.appmanager.metoo.packet;

import java.util.Map;

import sneer.kernel.appmanager.metoo.MeTooPacket;


public class AppListResponse implements MeTooPacket {

	private static final long serialVersionUID = 1L;

	public final Map<String, Long> _installNameAndSize;
	
	public AppListResponse(Map<String,Long> installNameAndSize){
		_installNameAndSize = installNameAndSize;
	}
	public int type() {
		return APP_LIST_RESPONSE; 
	}

}
