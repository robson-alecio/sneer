package sneer.skin.dashboard;

import java.awt.Container;

import sneer.skin.dashboard.impl.SnappFrame;
import sneer.skin.laf.LafContainer;
import sneer.skin.viewmanager.Snapp;

public interface Dashboard extends LafContainer{

	Container getRootPanel();
	Container getContentPanel();
	SnappFrame installSnapp(Snapp snapp);
}
