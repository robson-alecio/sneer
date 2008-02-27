package wheel.testutil;

import junit.framework.TestCase;

public abstract class TestOfInterface<SUBJECT> extends TestCase {

	protected SUBJECT _subject;

	@Override
	protected void setUp() {
		_subject = prepareSubject();
	}

	protected abstract SUBJECT prepareSubject();

}