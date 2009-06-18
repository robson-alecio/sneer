package sneer.bricks.skin.main.dashboard.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.Image;
import java.net.URL;

import sneer.bricks.hardware.gui.images.Images;
import sneer.bricks.skin.image.DefaultIcons;
import sneer.bricks.skin.image.ImageFactory;

class IconUtil{
	static Image getLogo() {
		return my(Images.class).getImage(	getLogoURL());
	}

	static URL getLogoURL() {
		return my(ImageFactory.class).getImageUrl(DefaultIcons.logo16x16);
	}
}