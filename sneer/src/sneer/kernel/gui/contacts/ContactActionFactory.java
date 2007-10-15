package sneer.kernel.gui.contacts;

import java.util.ArrayList;
import java.util.List;

import sneer.SystemApplications;
import sneer.kernel.api.SovereignApplication;
import sneer.kernel.appmanager.SovereignApplicationUID;

public class ContactActionFactory {

	private final SystemApplications _systemApplications;

	public ContactActionFactory(SystemApplications systemApplications){
		_systemApplications = systemApplications;
	}
	
	public List<ContactAction> contactActions(){
		List<ContactAction> result = new ArrayList<ContactAction>();
		
		registerContactActions(result,_systemApplications._conversations);
		registerContactActions(result,_systemApplications._fileTransfer);
		registerContactActions(result,_systemApplications._talk);
		registerContactActions(result,_systemApplications._meToo);
		registerContactActions(result,_systemApplications._sharedFolder);
		registerContactActions(result,_systemApplications._publicFiles);
		
		for(SovereignApplicationUID app:_systemApplications._appManager.publishedApps().output())
			if (app._sovereignApplication.contactActions()!=null)
				for(ContactAction contactAction:app._sovereignApplication.contactActions())
					result.add(contactAction);
		return result;
	}

	private void registerContactActions(List<ContactAction> result, SovereignApplication app) {
		for(ContactAction contactAction:app.contactActions())
			result.add(contactAction);
	}
}
