package sneer.remote;

import sneer.life.Life;
import wheel.experiments.environment.network.ObjectSocket;

interface Indian extends Query<String> {
	
	public void reportAbout(Life life, ObjectSocket socket);
	public void receive(SmokeSignal smokeSignal);
		
	public int id();
}
