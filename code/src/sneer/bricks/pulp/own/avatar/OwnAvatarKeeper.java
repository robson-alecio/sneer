package sneer.bricks.pulp.own.avatar;

import java.awt.Image;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface OwnAvatarKeeper {

	Signal<Image> avatar(int squareSize);

	Consumer<Image> avatarSetter();
}
