package spikes.bamboo;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;

import main.Sneer;
import sneer.brickness.testsupport.SystemBrickEnvironment;
import sneer.commons.environments.CachingEnvironment;
import sneer.commons.environments.Environments;
import spikes.bamboo.bricksorter.BrickSorter;

public class BrickSorterApplication {

	public static void main(String[] args) {
		
		Environments.runWith(new CachingEnvironment(new SystemBrickEnvironment()), new Runnable() { public void run() {
			try {
				for (Class<?> brick : my(BrickSorter.class).sort(Sneer.businessBricks()))
					System.out.println(brick);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}});
	}

}
