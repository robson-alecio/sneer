package sneer.skin.image_Selector.tests;

import java.awt.Image;

import sneer.kernel.container.ContainerUtils;
import sneer.skin.image_Selector.ImageSelector;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class ImageSelectorDemo  {

	public static void main(String[] args) throws Exception {
		sneer.kernel.container.Container container = ContainerUtils.getContainer();

		ImageSelector imageSelector = container.produce(ImageSelector.class);
		imageSelector.open(new Omnivore<Image>(){@Override public void consume(Image valueObject) {
			//OK
		}});

		Threads.sleepWithoutInterruptions(30000);
	}
}