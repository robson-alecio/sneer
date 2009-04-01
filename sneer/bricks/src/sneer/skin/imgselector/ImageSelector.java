package sneer.skin.imgselector;

import java.awt.Image;

import sneer.brickness.OldBrick;
import wheel.lang.Consumer;

public interface ImageSelector extends OldBrick {

	void open(Consumer<Image> avatarSetter);

}
