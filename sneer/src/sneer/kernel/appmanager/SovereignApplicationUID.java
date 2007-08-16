package sneer.kernel.appmanager;

public class SovereignApplicationUID {
	
	public final SovereignApplication _sovereignApplication;
	public final String _appUID; //created on demand by Sneer application

	public SovereignApplicationUID(SovereignApplication sovereignApplication, String appUID){
		_sovereignApplication = sovereignApplication;
		_appUID = appUID;
	}

}
