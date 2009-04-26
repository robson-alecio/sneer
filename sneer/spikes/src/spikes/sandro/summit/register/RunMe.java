package spikes.sandro.summit.register;

import sneer.brickness.impl.Brickness;
import sneer.pulp.events.EventNotifiers;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.reactive.Signals;
import spikes.sandro.summit.register.logger.SimpleLogger;
import wheel.io.Jars;

public class RunMe {
	
	Brickness container = new Brickness();
	
	RunMe(){
		placeBricks(new Class<?>[]{
				Signals.class,
				EventNotifiers.class,
				ExceptionHandler.class,
				
				SimpleRegister.class,
				SimpleLogger.class
		});
	}

	private void placeBricks(Class<?> bricks[]) {
		for (Class<?> brick : bricks) 
			placeBrick(brick);
	}

	private void placeBrick(Class<?> brick) {
		container.placeBrick(Jars.classpathRootFor(brick), brick.getName());
	}

	public static void main(String[] args) {
		new RunMe();
	}

}
