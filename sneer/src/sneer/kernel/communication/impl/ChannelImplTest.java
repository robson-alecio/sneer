package sneer.kernel.communication.impl;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.impl.ContactIdImpl;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

//Refactor: test what happens when there is a crash, if both sides are aware of it. if not, think about a way to let both sides find out.
public class ChannelImplTest {
	Channel _channel;
	public ChannelImplTest(){
		_channel = new ChannelImpl(output1(),this.getClass().getClassLoader());
		_channel.input().addReceiver(receiver1());
		
		ContactId contactId = new ContactIdImpl(123456);
		
		while(true){
			_channel.output().consume(new Packet(contactId,"It seems"));
			_channel.output().consume(new Packet(contactId,"to be"));
			Threads.sleepWithoutInterruptions(1000);
			_channel.output().consume(new Packet(contactId,"working"));
			Threads.sleepWithoutInterruptions(1000);
		}

	}
	
	private Omnivore<Packet> receiver1() {
		return new Omnivore<Packet>(){ @Override public void consume(Packet packet) {
			System.out.println("receiver channel 1: "+packet._contents);
			
		}};
	}

	private Omnivore<Packet> output1() {
		return new Omnivore<Packet>(){ @Override public void consume(Packet packet) {
			System.out.println("output 1: "+packet._contents);
			try{
				((ChannelImpl)_channel).receive(packet);
			}catch(Exception e){
				e.printStackTrace();
			}
		}};
	}

	public static void main(String[] args){
		new ChannelImplTest();
	}
	
}
