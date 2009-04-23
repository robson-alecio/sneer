package sneer.skin.imgselector;

import java.awt.Image;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Consumer;

@Brick
public interface ImageSelector {

	void open(Consumer<Image> avatarSetter);

}
