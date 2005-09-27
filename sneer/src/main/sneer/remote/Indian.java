package sneer.remote;

import sneer.life.LifeView;
import wheel.experiments.environment.network.ObjectSocket;

interface Indian extends Query<String> {
	
	public void reportAbout(LifeView life, ObjectSocket socket);
	public void receive(SmokeSignal smokeSignal);
		
	public int id();
}
