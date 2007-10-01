package sneer.kernel.appmanager;

import sneer.kernel.api.SovereignApplication;

public class SovereignApplicationUID {
	
	public final SovereignApplication _sovereignApplication;
	public final String _appUID; //created on demand by Sneer application
	public final String _installName; 

	public SovereignApplicationUID(String installName, String appUID, SovereignApplication sovereignApplication){
		_installName = installName;
		_sovereignApplication = sovereignApplication;
		_appUID = appUID;
	}

}
