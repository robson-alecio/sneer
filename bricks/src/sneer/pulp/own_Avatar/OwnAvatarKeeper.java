package sneer.pulp.own_Avatar;

import java.awt.Image;

import sneer.kernel.container.Brick;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface OwnAvatarKeeper extends Brick {

	Signal<Image> avatar(int squareSize);

	Omnivore<Image> avatarSetter();
}
