package sneer.snapp.owner;

import sneer.bricks.name.OwnNameKeeper;
import sneer.skin.viewmanager.Snapp;
import sneer.widgets.reactive.TextWidget;

public interface OwnerSnapp extends Snapp {

	OwnNameKeeper getOwnNameKeeper();
	TextWidget getEditableLabel();

}
