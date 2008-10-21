package sneer.skin.dashboard;

public enum InstrumentState {//Implement dashboard support for this states
	iconified, 		//Just a Icon in the Dashboard (one icon)
	minimized,		//A Icon and a Title in the Dashboard (one roll)
	attached,		//A Normal Instrument Gui in the Dashboard (one pane)
	deattached,	//Deattached Instrument Window (one window)
	maximized;	//Showing the Additional Instrument Window (one * and additional window)
}