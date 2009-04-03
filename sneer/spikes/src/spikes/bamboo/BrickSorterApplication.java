package spikes.bamboo;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.Sneer;
import sneer.brickness.testsupport.SystemBrickEnvironment;
import sneer.commons.environments.CachingEnvironment;
import sneer.commons.environments.Environments;
import spikes.bamboo.bricksorter.BrickSorter;

public class BrickSorterApplication {

	public static void main(String[] args) {
		
		Environments.runWith(new CachingEnvironment(new SystemBrickEnvironment()), new Runnable() { public void run() {
			try {
				List<Class<?>> originalBricks = new ArrayList<Class<?>>();
				for (Class<?> brick : Sneer.businessBricks())
					originalBricks.add(brick);
				
				for (Class<?> brick : my(BrickSorter.class).sort(originalBricks.toArray(new Class<?>[]{})))
					if (originalBricks.contains(brick))
						System.out.println(brick.getName() + ".class,");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}});
	}

}
