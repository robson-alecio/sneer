package wheel.io.files.impl.tranzient.tests;

import java.io.IOException;

import junit.framework.TestCase;
import wheel.io.files.impl.CloseableWithListener;
import wheel.io.files.impl.CloseableWithListener.Listener;

public abstract class CloseableStreamTestBase extends TestCase {

	private CloseableWithListener _subject;
	private CloseableWithListener _lastClosedStream;

	@Override
	protected void setUp() throws Exception {
		_subject = createSubject();
	}

	protected abstract CloseableWithListener createSubject();

	public void testClose() throws IOException {
		_subject.notifyOnClose(new Listener(){
			public void streamClosed(CloseableWithListener stream) {
				_lastClosedStream = stream;
			}
		});
		_subject.close();
		assertSame(_subject, _lastClosedStream);
	}

}
