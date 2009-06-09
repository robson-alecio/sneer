package sneer.bricks.skin.imgselector;

import java.awt.Image;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.foundation.brickness.Brick;

@Brick
public interface ImageSelector {

	void open(Consumer<Image> avatarSetter);

}
