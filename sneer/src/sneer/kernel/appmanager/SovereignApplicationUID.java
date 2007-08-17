package sneer.kernel.appmanager;

public class SovereignApplicationUID {
	
	public final SovereignApplication _sovereignApplication;
	public final String _appUID; //created on demand by Sneer application
	public final String _appName; //created during runtime. it must be a valid directory name

	public SovereignApplicationUID(String appName, String appUID, SovereignApplication sovereignApplication){
		_appName = appName;
		_sovereignApplication = sovereignApplication;
		_appUID = appUID;
	}

}
