package snapps.wind.tests;

import snapps.wind.Shout;

public class ShoutMock extends Shout {

	public final long _mockTime;

	public ShoutMock(String phrase_, long mockTime) {
		super(phrase_);
		_mockTime = mockTime;
	}

	@Override
	public long publicationTime() {
		return _mockTime;
	}
}