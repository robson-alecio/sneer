package sneer.pulp.own.avatar;

import java.awt.Image;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public interface OwnAvatarKeeper extends Brick {

	Signal<Image> avatar(int squareSize);

	Consumer<Image> avatarSetter();
}
