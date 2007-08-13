package sneer.kernel.appmanager;

import sneer.kernel.gui.contacts.ContactAction;

public interface App {
	
	public String name();
	
	public ContactAction contactAction();
	
	public void onStart(); //method called before running the first executionLoop
	
	public void onPause(); //method called when the app is paused
	
	public void onStop(); //method called when the app is stopped
	
	public void executionLoop(); //small units of execution, to allow start/pause/stop

}
