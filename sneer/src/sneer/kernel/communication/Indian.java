package sneer.kernel.communication;

import sneer.kernel.business.BusinessSource;
import wheel.io.network.ObjectSocket;

interface Indian extends Query<String> {
	
	public void reportAbout(BusinessSource business, ObjectSocket socket);
	public void receive(SmokeSignal smokeSignal);
		
	public int id();
}
