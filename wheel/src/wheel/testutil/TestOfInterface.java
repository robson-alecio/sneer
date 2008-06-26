package wheel.testutil;

import org.junit.Before;

public abstract class TestOfInterface<SUBJECT> {

	protected SUBJECT _subject;

	@Before
	public void initSubject() {
		_subject = prepareSubject();
	}

	protected abstract SUBJECT prepareSubject();

}