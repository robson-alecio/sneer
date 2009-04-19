package spikes.demos;

import java.awt.Image;

import sneer.brickness.testsupport.SystemBrickEnvironment;
import sneer.commons.environments.Environments;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.logging.out.LogToSystemOut;
import sneer.skin.imgselector.ImageSelector;
import wheel.lang.Threads;
import static sneer.commons.environments.Environments.my;



public class ImageSelectorDemo  {

	public static void main(String[] args) throws Exception {
		Environments.runWith(new SystemBrickEnvironment(), new Runnable(){ @Override public void run() {
			my(LogToSystemOut.class);
			
			try {
				ImageSelector imageSelector = Environments.my(ImageSelector.class);
				imageSelector.open(new Consumer<Image>(){@Override public void consume(Image valueObject) {
					//OK
				}});

				Threads.sleepWithoutInterruptions(30000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
	}
}