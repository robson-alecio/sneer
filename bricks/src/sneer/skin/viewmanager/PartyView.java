package sneer.skin.viewmanager;

import java.awt.Container;

import sneer.bricks.mesh.Party;
import wheel.reactive.Signal;

public interface PartyView {

	void init(Container container, Signal<Party> activeParty);

}
