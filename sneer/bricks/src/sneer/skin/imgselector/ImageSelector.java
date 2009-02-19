package sneer.skin.imgselector;

import java.awt.Image;

import sneer.kernel.container.Brick;
import wheel.lang.Consumer;

public interface ImageSelector extends Brick {

	void open(Consumer<Image> avatarSetter);

}
