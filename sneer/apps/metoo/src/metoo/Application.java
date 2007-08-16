package metoo;
import static wheel.i18n.Language.translate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import metoo.gui.MeTooFrame;
import sneer.kernel.appmanager.AppConfig;
import sneer.kernel.appmanager.SovereignApplication;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.Action;
import wheel.lang.Omnivore;


public class Application implements SovereignApplication{
	
	private final Channel _channel;
	
	public Application(AppConfig appConfig){
		_channel = appConfig._channelFactory.channel(this);
		_channel.input().addReceiver(receiver());
	}

	private Omnivore<Packet> receiver() {
		return new Omnivore<Packet>(){
			public void consume(Packet valueObject) {
				System.out.println("received : "+valueObject);
			}
		};
	}

	public List<ContactAction> contactActions() {
		Collections.singletonList(new ContactAction(){
			public void actUpon(Contact contact) {
				openMeTooFrame(contact);
			}
			public String caption() {
				return translate("Me Too");
			}
		});
		
		return null;
	}
	
	private final Map<ContactId, MeTooFrame> _framesByContactId = new HashMap<ContactId, MeTooFrame>();

	protected void openMeTooFrame(Contact contact) {
		if (_framesByContactId.get(contact.id()) == null)
			_framesByContactId.put(contact.id(), new MeTooFrame(_channel,contact));
		else
			_framesByContactId.get(contact.id()).sendAppListRequest();
		_framesByContactId.get(contact.id()).setVisible(true);
	}

	public String defaultName() {
		return "meetoo";
	}

	public List<Action> mainActions() {
		return null;
	}

	public int trafficPriority() {
		return 1;
	}

}
