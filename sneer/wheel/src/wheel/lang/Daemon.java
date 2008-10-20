package wheel.lang;

import java.util.HashSet;
import java.util.Set;

public abstract class Daemon extends Thread {

	static private final Set<Daemon> _instances = new HashSet<Daemon>(); 
	
	public Daemon(String name) {
		super(name);
		setDaemon(true);
		start();
		addInstance(this);
	}

	synchronized static private void addInstance(Daemon instance) {
		_instances.add(instance);
	}

	@SuppressWarnings("deprecation")
	synchronized static public void killAllInstances() {
		for (Daemon victim : _instances)
			victim.stop();
		
		_instances.clear();
	}
	
}
