package sneer.skin.imageSelector.tests;

import sneer.lego.ContainerUtils;
import sneer.skin.imageSelector.ImageSelector;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.napkin.NapkinLafSupport;
import wheel.lang.Threads;

public class ImageSelectorDemo  {

	public static void main(String[] args) throws Exception {
		sneer.lego.Container container = ContainerUtils.getContainer();

		NapkinLafSupport tmp = container.produce(NapkinLafSupport.class);
		LafManager reg = container.produce(LafManager.class);
		reg.setActiveLafSupport(tmp);

		ImageSelector imageSelector = container.produce(ImageSelector.class);
		imageSelector.getImageIcon();

		Threads.sleepWithoutInterruptions(30000);
	}
}