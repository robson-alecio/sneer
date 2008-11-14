package sneer.skin.imgselector;

import java.awt.Image;

import wheel.lang.Consumer;

public interface ImageSelector {

	void open(Consumer<Image> avatarSetter);

}
