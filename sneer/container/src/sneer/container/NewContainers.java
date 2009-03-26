package sneer.container;

import sneer.container.impl.NewContainerImpl;

public class NewContainers {

	public static NewContainer newContainer() {
		return new NewContainerImpl();
	}

}
