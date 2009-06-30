package spikes.sneer.bricks.pulp.own.avatar.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.Image;

import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.image.ImageFactory;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import spikes.sneer.bricks.pulp.own.avatar.OwnAvatarKeeper;

class OwnAvatarKeeperImpl implements OwnAvatarKeeper {

	private Register<Image> _avatar = my(Signals.class).newRegister(null);
	
	private final ImageFactory _imageFactory = my(ImageFactory.class);

	@Override
	public Signal<Image> avatar(final int squareSize) {
		return my(Signals.class).adapt(_avatar.output(), new Functor<Image, Image>() { @Override public Image evaluate(Image original) {
			try {
				return _imageFactory.getScaledInstance(original, squareSize, squareSize);
			} catch (Hiccup e) {
				return null;
				// Fix We cannot simply swallow this hiccup. Big design changes are necessary.
			}
		}});
	}

	@Override
	public Consumer<Image> avatarSetter() {
		return _avatar.setter();
	}
}
