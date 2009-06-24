package sneer.bricks.snapps.owninfo.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.snapps.owninfo.OwnInfo;
import sneer.bricks.software.bricks.statestore.BrickStateStore;

class Store{
	
	private final int TIMEOUT = 30*1000;
	
	private final int OWN_NAME = 0;
	private final int SNEER_PORT = 1;
	private final int DYNDNS_HOST = 2;
	private final int DYNDNS_USER = 3;
	private final int DYNDNS_PASSWORD = 4;
	
	private Object[] _ownInfoData = null;

	Store(){
		try {
			_ownInfoData  = (Object[]) my(BrickStateStore.class).readObjectFor(OwnInfo.class, getClass().getClassLoader());
		} catch (Throwable e) {
			BlinkingLights bl = my(BlinkingLights.class);
			bl.  turnOn(LightType.WARN, "Unable to restore your personal info.", "Please inform your personal info.", e, TIMEOUT);
		} 
	 }

	boolean restoreFail() {
		return _ownInfoData==null;
	}

	void save(String name, int port, String dynHost, String dynUser, String dynPassword) {
		try {
			Object[] data = new Object[]{name, port, dynHost, dynUser, dynPassword};
			my(BrickStateStore.class).writeObjectFor(OwnInfo.class, data);
			_ownInfoData = data;
		} catch (Exception e) {
			BlinkingLights bl = my(BlinkingLights.class);
			bl.turnOn(LightType.ERROR, "Unable to store Contacts", null, e, TIMEOUT);
		}
	 }

	Integer sneerPort() { return (Integer) _ownInfoData[SNEER_PORT]; }
	String ownName() { return (String) _ownInfoData[OWN_NAME]; }
	String dynDnsHost() { return (String) _ownInfoData[DYNDNS_HOST]; }
	String dynDnsPassword() { return (String) _ownInfoData[DYNDNS_PASSWORD]; }
	String dynDnsUser() { return (String) _ownInfoData[DYNDNS_USER]; }
}