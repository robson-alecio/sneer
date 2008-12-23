package sneer.kernel.container.tests;

import sneer.skin.GuiBrick;

public interface SomeGuiBrick extends GuiBrick {

	Thread currentThread();

	void slowMethod();

}
