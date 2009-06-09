package sneer.pulp.own.avatar;

import java.awt.Image;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;

@Brick
public interface OwnAvatarKeeper {

	Signal<Image> avatar(int squareSize);

	Consumer<Image> avatarSetter();
}
