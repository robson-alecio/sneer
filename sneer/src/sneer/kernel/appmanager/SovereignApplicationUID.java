package sneer.kernel.appmanager;

public class SovereignApplicationUID {
	
	public final SovereignApplication _sovereignApplication;
	public final String _appUID; //created on demand by Sneer application
	public final String _installName;
	public final SovereignApplicationInfo _info; 

	public SovereignApplicationUID(String installName, String appUID, SovereignApplication sovereignApplication, SovereignApplicationInfo info){
		_installName = installName;
		_sovereignApplication = sovereignApplication;
		_appUID = appUID;
		_info = info;
	}

}
