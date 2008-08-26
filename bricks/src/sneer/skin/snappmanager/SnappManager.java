package sneer.skin.snappmanager;

import sneer.kernel.container.Brick;
import sneer.skin.viewmanager.Snapp;
import wheel.reactive.lists.ListSignal;

public interface SnappManager extends Brick {

	void registerSnapp(Snapp snapp);

	ListSignal<Snapp> installedSnapps();
}
