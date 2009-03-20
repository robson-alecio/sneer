package sneer.pulp.own.avatar.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.Image;

import sneer.commons.lang.Functor;
import sneer.pulp.own.avatar.OwnAvatarKeeper;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.skin.image.ImageFactory;
import wheel.lang.Consumer;
import wheel.lang.exceptions.Hiccup;
import wheel.reactive.impl.Adapter;
import wheel.reactive.impl.RegisterImpl;

class OwnAvatarKeeperImpl implements OwnAvatarKeeper {

	private Register<Image> _avatar = new RegisterImpl<Image>(null);
	
	private final ImageFactory _imageFactory = my(ImageFactory.class);

	@Override
	public Signal<Image> avatar(final int squareSize) {
		Adapter<Image, Image> result = new Adapter<Image, Image>(_avatar.output(), new Functor<Image, Image>() { @Override public Image evaluate(Image original) {
			try {
				return _imageFactory.getScaledInstance(original, squareSize, squareSize);
			} catch (Hiccup e) {
				return null;
				// Fix We cannot simply swallow this hiccup. Big design changes are necessary.
			}
		}});
		return result.output();
	}

	@Override
	public Consumer<Image> avatarSetter() {
		return _avatar.setter();
	}
}
