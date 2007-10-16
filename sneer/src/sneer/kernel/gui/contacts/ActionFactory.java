package sneer.kernel.gui.contacts;

import java.util.ArrayList;
import java.util.List;

import sneer.SystemApplications;
import sneer.kernel.appmanager.SovereignApplicationUID;
import wheel.io.ui.Action;

public class ActionFactory {

	private final SystemApplications _systemApplications;

	public ActionFactory(SystemApplications systemApplications){
		_systemApplications = systemApplications;
	}
	
	public List<Action> mainActions(){
		List<Action> result = new ArrayList<Action>();
		
		registerActions(result,_systemApplications._conversations.mainActions());
		registerActions(result,_systemApplications._fileTransfer.mainActions());
		registerActions(result,_systemApplications._talk.mainActions());
		registerActions(result,_systemApplications._meToo.mainActions());
		registerActions(result,_systemApplications._sharedFolder.mainActions());
		registerActions(result,_systemApplications._publicFiles.mainActions());
		
		for(SovereignApplicationUID app:_systemApplications._appManager.publishedApps().output())
			registerActions(result, app._sovereignApplication.mainActions());
			
		return result;
	}
	
	public List<ContactAction> contactActions(){
		List<ContactAction> result = new ArrayList<ContactAction>();
		
		registerActions(result,_systemApplications._conversations.contactActions());
		registerActions(result,_systemApplications._fileTransfer.contactActions());
		registerActions(result,_systemApplications._talk.contactActions());
		registerActions(result,_systemApplications._meToo.contactActions());
		registerActions(result,_systemApplications._sharedFolder.contactActions());
		registerActions(result,_systemApplications._publicFiles.contactActions());
		
		for(SovereignApplicationUID app:_systemApplications._appManager.publishedApps().output())
			registerActions(result, app._sovereignApplication.contactActions());
			
		return result;
	}
	
	public List<DropAction> dropActions(){
		List<DropAction> result = new ArrayList<DropAction>();
		
		registerActions(result,_systemApplications._conversations.dropActions());
		registerActions(result,_systemApplications._fileTransfer.dropActions());
		registerActions(result,_systemApplications._talk.dropActions());
		registerActions(result,_systemApplications._meToo.dropActions());
		registerActions(result,_systemApplications._sharedFolder.dropActions());
		registerActions(result,_systemApplications._publicFiles.dropActions());
		
		for(SovereignApplicationUID app:_systemApplications._appManager.publishedApps().output())
			registerActions(result, app._sovereignApplication.dropActions());
			
		return result;
	}

	private <U> void registerActions(List<U> result, List<U> appList) {
		for(U action:appList)
			result.add(action);
	}
	
}
