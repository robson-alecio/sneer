package sneer.pulp.own.avatar.impl;

import java.awt.Image;

import sneer.kernel.container.Inject;
import sneer.pulp.own.avatar.OwnAvatarKeeper;
import sneer.skin.image.ImageFactory;
import wheel.lang.Functor;
import wheel.lang.Consumer;
import wheel.lang.exceptions.Hiccup;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.Adapter;
import wheel.reactive.impl.RegisterImpl;

class OwnAvatarKeeperImpl implements OwnAvatarKeeper {

	private Register<Image> _avatar = new RegisterImpl<Image>(null);
	
	@Inject
	private static ImageFactory _imageFactory;

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
