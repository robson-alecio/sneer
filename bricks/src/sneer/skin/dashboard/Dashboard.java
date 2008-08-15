package sneer.skin.dashboard;

import java.awt.Container;

import sneer.skin.viewmanager.Snapp;

public interface Dashboard{

	Container getRootPanel();
	Container getContentPanel();
	SnappFrame installSnapp(Snapp snapp);
	void moveSnapp(int index, SnappFrame frame);
	void moveSnappUp(SnappFrame frame);
	void moveSnappDown(SnappFrame frame);
	
}
