package sneer.pulp.own.avatar;

import java.awt.Image;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public interface OwnAvatarKeeper extends OldBrick {

	Signal<Image> avatar(int squareSize);

	Consumer<Image> avatarSetter();
}
