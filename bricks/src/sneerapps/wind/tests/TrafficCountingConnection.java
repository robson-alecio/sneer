package sneerapps.wind.tests;

import sneerapps.wind.Connection;
import sneerapps.wind.ConnectionSide;

public class TrafficCountingConnection implements Connection {

	Connection _delegate;

	public ConnectionSide sideA() {
		return _delegate.sideA();
	}

	public ConnectionSide sideB() {
		return _delegate.sideB();
	}

}
