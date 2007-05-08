package sneer.kernel.communication;

import sneer.kernel.business.Business;
import wheel.io.network.ObjectSocket;

interface Indian extends Query<String> {
	
	public void reportAbout(Business business, ObjectSocket socket);
	public void receive(SmokeSignal smokeSignal);
		
	public int id();
}
