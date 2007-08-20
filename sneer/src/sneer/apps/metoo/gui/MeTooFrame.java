package sneer.apps.metoo.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sneer.apps.metoo.MeTooPacket;
import sneer.apps.metoo.packet.AppListRequest;
import sneer.apps.metoo.packet.AppListResponse;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.pointofview.Contact;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class MeTooFrame extends JFrame{

	private final Channel _channel;
	private final Contact _contact;
	private final Signal<MeTooPacket> _input;

	private JList _appList;
	private DefaultListModel _listModel = new DefaultListModel();
	private Map<String, String> _nameAndAppUID = new HashMap<String, String>();

	public MeTooFrame(Channel channel, Contact contact, Signal<MeTooPacket> input){
		_channel = channel;
		_contact = contact;
		_input = input;
		_input.addReceiver(meTooPacketReceiver());
		initComponents();
		sendAppListRequest();
		
	}
	
	private void initComponents() {
		setLayout(new BorderLayout());
		setTitle(translate("Disponible Applications"));
		_appList = new JList(_listModel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		add(new JScrollPane(_appList),BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.EAST);
		setSize(300,400);
		setVisible(true);
	}

	private Omnivore<MeTooPacket> meTooPacketReceiver() {
		return new Omnivore<MeTooPacket>(){
			public void consume(MeTooPacket meTooPacket) {
				switch(meTooPacket.type()){
				case MeTooPacket.APP_LIST_RESPONSE:
					receiveAppListResponse(((AppListResponse)meTooPacket)._nameAndAppUID);
					break;
				}
			}
		};
	}
	
	protected void receiveAppListResponse(Map<String, String> nameAndAppUID) {
		_nameAndAppUID = nameAndAppUID;
		_listModel.clear();
		for(String key:_nameAndAppUID.keySet())
			_listModel.add(0, key);
	}

	public void sendAppListRequest(){
		_channel.output().consume(new Packet(_contact.id(),new AppListRequest()));
	}
	
	private static final long serialVersionUID = 1L;

}
