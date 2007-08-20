package sneer.apps.metoo;
import static wheel.i18n.Language.translate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import sneer.apps.metoo.gui.MeTooFrame;
import sneer.apps.metoo.packet.AppListResponse;
import sneer.kernel.appmanager.SovereignApplication;
import sneer.kernel.appmanager.SovereignApplicationUID;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.Action;
import wheel.lang.Omnivore;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;


public class MeToo implements SovereignApplication{
	
	private final Channel _channel;
	private final ListSignal<SovereignApplicationUID> _publishedApps;

	public MeToo(Channel channel, ListSignal<SovereignApplicationUID> publishedApps){
		_channel = channel;
		_publishedApps = publishedApps;
		_channel.input().addReceiver(meTooPacketReceiver());
	}

	private Omnivore<Packet> meTooPacketReceiver() {
		return new Omnivore<Packet>(){
			public void consume(Packet packet) {
				MeTooPacket meTooPacket = (MeTooPacket)packet._contents;
				switch(meTooPacket.type()){
					case MeTooPacket.APP_LIST_REQUEST:
						sendAppListResponse(packet._contactId);
						break;
				}
				SourceImpl<MeTooPacket> input = _inputsByContactId.get(packet._contactId);
				if (input == null) return;
				input.setter().consume(meTooPacket);
			}
		};
	}
	
	protected void sendAppListResponse(ContactId contactId) {
		Map<String,String> nameAndAppUID = new Hashtable<String,String>();
		for(SovereignApplicationUID app:_publishedApps)
			nameAndAppUID.put(app._sovereignApplication.defaultName(), app._appUID);
		_channel.output().consume(new Packet(contactId,new AppListResponse(nameAndAppUID)));
	}

	public List<ContactAction> contactActions() {
		return Collections.singletonList( (ContactAction)new ContactAction(){
			public void actUpon(Contact contact) {
				openMeTooFrame(contact);
			}
			public String caption() {
				return translate("Me Too");
			}
		});
	}
	
	private final Map<ContactId, MeTooFrame> _framesByContactId = new HashMap<ContactId, MeTooFrame>();
	private final Map<ContactId, SourceImpl<MeTooPacket>> _inputsByContactId = new HashMap<ContactId, SourceImpl<MeTooPacket>>();

	protected void openMeTooFrame(Contact contact) {
		if (_framesByContactId.get(contact.id()) == null){
			SourceImpl<MeTooPacket> input = new SourceImpl<MeTooPacket>(null);
			_inputsByContactId.put(contact.id(), input);
			_framesByContactId.put(contact.id(), new MeTooFrame(_channel, contact, input.output()));
		} else {
			_framesByContactId.get(contact.id()).sendAppListRequest();
		}
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
