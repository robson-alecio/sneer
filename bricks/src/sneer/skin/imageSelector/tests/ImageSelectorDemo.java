package sneer.skin.imageSelector.tests;

import java.awt.Image;

import sneer.lego.ContainerUtils;
import sneer.skin.imageSelector.ImageSelector;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.napkin.NapkinLafSupport;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class ImageSelectorDemo  {

	public static void main(String[] args) throws Exception {
		sneer.lego.Container container = ContainerUtils.getContainer();

		NapkinLafSupport tmp = container.produce(NapkinLafSupport.class);
		LafManager reg = container.produce(LafManager.class);
		reg.setActiveLafSupport(tmp);

		ImageSelector imageSelector = container.produce(ImageSelector.class);
		imageSelector.open(new Omnivore<Image>(){@Override public void consume(Image valueObject) {
			//OK
		}});

		Threads.sleepWithoutInterruptions(30000);
	}
}