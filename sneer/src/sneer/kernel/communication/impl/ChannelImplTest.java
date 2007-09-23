package sneer.kernel.communication.impl;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.impl.ContactIdImpl;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class ChannelImplTest {
	Channel channel;
	Channel channel2;
	public ChannelImplTest(){
		channel = new ChannelImpl(output1(),this.getClass().getClassLoader());
		channel.input().addReceiver(receiver1());
		
		ContactId contactId = new ContactIdImpl(123456);
		
		while(true){
			channel.output().consume(new Packet(contactId,"It seems"));
			channel.output().consume(new Packet(contactId,"to be"));
			Threads.sleepWithoutInterruptions(1000);
			channel.output().consume(new Packet(contactId,"working"));
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
				((ChannelImpl)channel).receive(packet);
			}catch(Exception e){
				e.printStackTrace();
			}
		}};
	}

	public static void main(String[] args){
		new ChannelImplTest();
	}
	
}
