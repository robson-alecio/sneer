package sneer.pulp.own.avatar;

import java.awt.Image;

import sneer.kernel.container.Brick;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface OwnAvatarKeeper extends Brick {

	Signal<Image> avatar(int squareSize);

	Consumer<Image> avatarSetter();
}
