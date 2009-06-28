package myfirst.snapp.impl;

import static sneer.foundation.environments.Environments.my;
import myfirst.brick.MyFirstBrick;
import myfirst.snapp.MyFirstSnapp;
import sneer.bricks.skin.main.menu.MainMenu;

public class MyFirstSnappImpl implements MyFirstSnapp {

	{
		my(MainMenu.class).addAction("My First Snapp", new Runnable() { @Override public void run() { //Who said Java doesn't have "Closures"? :)
			my(MyFirstBrick.class).helloWorld();
		}});
	}
	
}
