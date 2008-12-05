package sneer.pulp.streams.sequencer.tests;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.kernel.container.Inject;
import sneer.pulp.streams.sequencer.Sequencer;
import sneer.pulp.streams.sequencer.Sequencers;
import tests.JMockContainerEnvironment;
import tests.TestThatIsInjected;
import wheel.lang.Consumer;

@RunWith(JMockContainerEnvironment.class)
public class SequencerTest extends TestThatIsInjected {
	
	private static final short MAX_GAP = (short)500;

	private static final short BUFFER_SIZE = (short)30;

	@Inject private static Sequencers _subject;
	
	private final List<String> _recordedSequence = new ArrayList<String>();

	private final Mockery _mockery = new JUnit4Mockery();
	
	private final Consumer<String> _consumer = _mockery.mock(Consumer.class);

	@Test
	public void correctOrder() throws Exception {
		sequence("B", 1, "C", 2);
	}

	@Test
	public void inverseOrder() throws Exception {
		sequence("C", 2, "B", 1);
	}

	private void sequence(String packet1, int sequence1, String packet2, int sequence2) {
		_mockery.checking(new Expectations(){{
			Sequence main = _mockery.sequence("main");
			one(_consumer).consume("A"); inSequence(main);
			one(_consumer).consume("B"); inSequence(main);
			one(_consumer).consume("C"); inSequence(main);
			one(_consumer).consume("D"); inSequence(main);
		}});
		
		Sequencer<String> sequencer = _subject.createSequencerFor(_consumer, BUFFER_SIZE, MAX_GAP);

		sequencer.sequence("A", (short)0);
		sequencer.sequence(packet1, (short)sequence1);
		sequencer.sequence(packet2, (short)sequence2);
		sequencer.sequence("D", (short)3);
	}
	

	@Test
	public void sequencing() {
		int[] input = new int[] {
			0, 1, 2, 3, //Happy
			-1, 2, // Less than 500 negative difference: discarded
			6, 4, 5, //Different order, no gap
			7, 11, 10, 9, 8, //Different order, no gap
			20, 23, 21, //Different order, with gap: will wait
			23, // Already waiting: discard
			51, // More than 30 gap since last played (11): will cause 20, 21 to be played.
			600, 601, 602, // More than 500 gap: will cause buffer to drain (23, 51 will not be played)
			604, //Gap
			-3, -2, -1, // More than 500 gap in the other direction will also cause buffer to drain (604 will not be played)
			Short.MAX_VALUE-2, Short.MAX_VALUE, Short.MAX_VALUE-1,
			Short.MIN_VALUE+2, Short.MIN_VALUE, Short.MIN_VALUE +1
		};

		feedInputSequence(input);
		expectOutputSequence(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 20, 21, 600, 601, 602, -3, -2, -1,
				Short.MAX_VALUE-2, Short.MAX_VALUE-1, Short.MAX_VALUE,
				Short.MIN_VALUE, Short.MIN_VALUE+1, Short.MIN_VALUE+2);
	}

	private void feedInputSequence(int[] input) {
		Sequencer<String> sequencer = _subject.createSequencerFor(sequenceRecorder(), BUFFER_SIZE, MAX_GAP);
		
		for (int sequence : input)
			sequencer.sequence(""+sequence, (short)sequence);
	}
	
	private Consumer<String> sequenceRecorder() {
		return new Consumer<String>(){ @Override public void consume(String value) {
			_recordedSequence.add(value);
		}};
	}
	
	private void expectOutputSequence(int... expected) {
		for (int i = 0; i < expected.length; i++)
			assertEquals(""+expected[i], _recordedSequence.get(i)); 
	}
	
}