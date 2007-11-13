package addressbook;

import java.util.Collections;
import java.util.List;

import addressbook.gui.AddressBookFrame;

import sneer.kernel.api.SovereignApplicationNeeds;
import wheel.io.ui.Action;
import static wheel.i18n.Language.*;

public class AddressBookApp {

	private AddressBookFrame _addressBookFrame;
	private final SovereignApplicationNeeds _config;

	public AddressBookApp(SovereignApplicationNeeds config) {
		_config = config;
	}

	public List<Action> mainActions() {
		return Collections.singletonList((Action)new Action(){
			public String caption() {
				return translate("Address Book");
			}
			public void run() {
				openGui();
			}
		});
	}
	
	private void openGui() {
		if (_addressBookFrame==null)
			_addressBookFrame = new AddressBookFrame(_config);
		_addressBookFrame.setVisible(true);
	}

}
