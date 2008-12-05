package sneer.pulp.streams.sequencer.tests;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.kernel.container.Inject;
import sneer.pulp.streams.sequencer.Sequencer;
import sneer.pulp.streams.sequencer.Sequencers;
import tests.JMockContainerEnvironment;
import tests.TestThatIsInjected;
import wheel.lang.Consumer;

@RunWith(JMockContainerEnvironment.class)
public class SpeakerBufferTest extends TestThatIsInjected {
	
	@Inject private static Sequencers _subject;
	
	private final List<Integer> _recordedSequence = new ArrayList<Integer>();

	private final Mockery _mockery = new JUnit4Mockery();
	
	private final Consumer<Integer> _consumer = _mockery.mock(Consumer.class);

	@Test
	public void correctOrder() throws Exception {
		testBuffering(p1(), p2());
	}

	@Test
	@Ignore
	public void inverseOrder() throws Exception {
		testBuffering(p2(), p1());
	}

	private void testBuffering(Integer first, Integer second) {
		_mockery.checking(new Expectations(){{
			Sequence main = _mockery.sequence("main");
			one(_consumer).consume(p0()); inSequence(main);
			one(_consumer).consume(p1()); inSequence(main);
			one(_consumer).consume(p2()); inSequence(main);
			one(_consumer).consume(p3()); inSequence(main);
		}});
		
		Sequencer<Integer> buffer = _subject.createSequencerFor(_consumer);
		Integer initialPacket = p0();
		Integer lastPacket = p3();

		buffer.sequence(initialPacket, (short)0);
		buffer.sequence(first,  (short)1);
		buffer.sequence(second,  (short)2);
		buffer.sequence(lastPacket, (short)3);
	}
	
	@Test
	@Ignore
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
		Sequencer<Integer> buffer = _subject.createSequencerFor(sequenceRecorder());
		
		short counter = 0;
		for (int sequence : input)
			buffer.sequence(sequence, counter++);
	}
	
	private Consumer<Integer> sequenceRecorder() {
		return new Consumer<Integer>(){ @Override public void consume(Integer value) {
			_recordedSequence.add(value);
		}};
	}
	
	private void expectOutputSequence(int... sequences) {
		for (int i = 0; i < sequences.length; i++)
			assertEquals((int)_recordedSequence.get(i), (Object)sequences[i]); 
	}
	
	private Integer p0() {
		return 10;
	}
	
	private Integer p1() {
		return 20;
	}
	
	private Integer p2() {
		return 30;
	}
	
	private Integer p3() {
		return 44;
	}
}