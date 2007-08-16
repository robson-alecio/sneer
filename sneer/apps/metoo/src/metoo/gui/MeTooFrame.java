package metoo.gui;

import javax.swing.JFrame;

import metoo.packet.AppListRequest;

import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.pointofview.Contact;

public class MeTooFrame extends JFrame{

	private final Channel _channel;
	private final Contact _contact;


	public MeTooFrame(Channel channel, Contact contact){
		_channel = channel;
		_contact = contact;
		sendAppListRequest();
	}
	
	public void sendAppListRequest(){
		_channel.output().consume(new Packet(_contact.id(),new AppListRequest()));
	}
	
	private static final long serialVersionUID = 1L;

}
