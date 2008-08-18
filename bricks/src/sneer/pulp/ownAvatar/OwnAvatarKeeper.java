package sneer.pulp.ownAvatar;

import java.awt.Image;

import sneer.lego.Brick;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface OwnAvatarKeeper extends Brick {

	Signal<Image> avatar(int squareSize);

	Omnivore<Image> avatarSetter();
}
