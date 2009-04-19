package spikes.sandro.swing.synth;

import static sneer.commons.environments.Environments.my;

import java.awt.Image;
import java.net.URL;

import sneer.hardware.gui.images.Images;
import sneer.skin.image.DefaultIcons;
import sneer.skin.image.ImageFactory;

class IconUtil{
	static Image getLogo() {
		return my(Images.class).getImage(	getLogoURL());
	}

	static URL getLogoURL() {
		return my(ImageFactory.class).getImageUrl(DefaultIcons.logo16x16);
	}
}