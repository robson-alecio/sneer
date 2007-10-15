package sneer.kernel.gui.contacts;

import java.util.ArrayList;
import java.util.List;

import sneer.SystemApplications;
import sneer.kernel.api.SovereignApplication;
import sneer.kernel.appmanager.SovereignApplicationUID;

public class DropActionFactory {

	private final SystemApplications _systemApplications;

	public DropActionFactory(SystemApplications systemApplications){
		_systemApplications = systemApplications;
	}
	
	
	public List<DropAction> dropActions(){
		List<DropAction> result = new ArrayList<DropAction>();
		
		registerDropActions(result,_systemApplications._conversations);
		registerDropActions(result,_systemApplications._fileTransfer);
		registerDropActions(result,_systemApplications._talk);
		
		for(DropAction dropAction:_systemApplications._meToo.dropActions())
			result.add(dropAction);
		for(DropAction dropAction:_systemApplications._sharedFolder.dropActions())
			result.add(dropAction);
		for(DropAction dropAction:_systemApplications._publicFiles.dropActions())
			result.add(dropAction);
		
		for(SovereignApplicationUID app:_systemApplications._appManager.publishedApps().output())
			if (app._sovereignApplication.contactActions()!=null)
				for(DropAction dropAction:app._sovereignApplication.dropActions())
					result.add(dropAction);
		return result;
	}

	private void registerDropActions(List<DropAction> result, SovereignApplication app) {
		for(DropAction dropAction:app.dropActions())
			result.add(dropAction);
	}
}
