package sneer.pulp.ownAvatar.impl;

import java.awt.Image;

import sneer.kernel.container.Inject;
import sneer.pulp.ownAvatar.OwnAvatarKeeper;
import sneer.skin.image.ImageFactory;
import wheel.lang.Functor;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.Adapter;
import wheel.reactive.impl.RegisterImpl;

public class OwnAvatarKeeperImpl implements OwnAvatarKeeper {

	private Register<Image> _avatar = new RegisterImpl<Image>(null);
	
	@Inject
	private static ImageFactory _imageFactory;

	@Override
	public Signal<Image> avatar(final int squareSize) {
		Adapter<Image, Image> result = new Adapter<Image, Image>(_avatar.output(), new Functor<Image, Image>() { @Override public Image evaluate(Image original) {
			return _imageFactory.getScaledInstance(original, squareSize, squareSize);
		}});
		return result.output();
	}

	@Override
	public Omnivore<Image> avatarSetter() {
		return _avatar.setter();
	}
}
