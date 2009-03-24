package spikes.demos;

import java.awt.Image;

import sneer.commons.environments.Environments;
import sneer.kernel.container.Containers;
import sneer.skin.imgselector.ImageSelector;
import wheel.io.Logger;
import wheel.lang.Consumer;
import wheel.lang.Threads;

public class ImageSelectorDemo  {

	public static void main(String[] args) throws Exception {
		Logger.redirectTo(System.out);
		Environments.runWith(Containers.newContainer(), new Runnable(){ @Override public void run() {
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