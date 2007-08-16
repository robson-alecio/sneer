package sneer.kernel.appmanager;

public class SovereignApplicationUID {
	
	public final SovereignApplication _sovereignApplication;
	public final String _uid; //created on demand by Sneer application

	public SovereignApplicationUID(SovereignApplication sovereignApplication, String UID){
		_sovereignApplication = sovereignApplication;
		_uid = UID;
	}

}
