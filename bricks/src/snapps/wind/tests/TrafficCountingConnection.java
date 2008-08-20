package snapps.wind.tests;

import snapps.wind.Connection;
import snapps.wind.ConnectionSide;

public class TrafficCountingConnection implements Connection {

	Connection _delegate;

	public ConnectionSide sideA() {
		return _delegate.sideA();
	}

	public ConnectionSide sideB() {
		return _delegate.sideB();
	}

}
