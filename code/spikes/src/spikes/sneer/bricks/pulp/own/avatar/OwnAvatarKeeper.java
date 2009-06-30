package spikes.sneer.bricks.pulp.own.avatar;

import java.awt.Image;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface OwnAvatarKeeper {

	Signal<Image> avatar(int squareSize);

	Consumer<Image> avatarSetter();
}
