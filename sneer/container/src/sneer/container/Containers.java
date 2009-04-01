package sneer.container;

import sneer.container.impl.ContainerImpl;

public class Containers {

	public static Container newContainer(Object... bindings) {
		return new ContainerImpl(bindings);
	}

}
