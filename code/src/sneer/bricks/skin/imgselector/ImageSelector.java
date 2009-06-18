package sneer.bricks.skin.imgselector;

import java.awt.Image;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface ImageSelector {

	void open(Consumer<Image> avatarSetter);

}
