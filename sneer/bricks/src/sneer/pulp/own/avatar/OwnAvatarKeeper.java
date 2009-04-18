package sneer.pulp.own.avatar;

import java.awt.Image;

import sneer.brickness.OldBrick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;

public interface OwnAvatarKeeper extends OldBrick {

	Signal<Image> avatar(int squareSize);

	Consumer<Image> avatarSetter();
}
