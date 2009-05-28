package spikes.demos;

import static sneer.commons.environments.Environments.my;

import java.awt.Image;

import sneer.brickness.Brickness;
import sneer.commons.environments.Environments;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.threads.Threads;
import sneer.skin.imgselector.ImageSelector;



public class ImageSelectorDemo  {

	public static void main(String[] args) throws Exception {
		Environments.runWith(Brickness.newBrickContainer(), new Runnable(){ @Override public void run() {
			try {
				ImageSelector imageSelector = Environments.my(ImageSelector.class);
				imageSelector.open(new Consumer<Image>(){@Override public void consume(Image valueObject) {
					//OK
				}});

				my(Threads.class).sleepWithoutInterruptions(30000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
	}
}