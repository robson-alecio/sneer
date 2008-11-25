package spikes.bamboo;

import static org.junit.Assert.assertEquals;

import javax.sound.sampled.TargetDataLine;

import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.mic.Mic;
import wheel.lang.Consumer;

@RunWith(Mocotoh.class)
public class NewStyleMicTest {

	@Test
	public void test() throws Exception {
		
		new TestSpec() {
			
			final Mic mic = my(Mic.class);
			final TupleSpace tuples = my(TupleSpace.class);
			final ThreadPool threads = mock();
			final Audio audio = mock();
			TargetDataLine line;
			
			final Consumer<PcmSoundPacket> subscriber = mock();
			
			@Override
			public void stimuli() throws Exception {	
				new Stimulus() {{
					tuples.addSubscription(PcmSoundPacket.class, subscriber);
				}};
	
				final Stepper stepper = capture();
				new Stimulus() {{
					mic.open();
						threads.registerStepper(stepper);
				}};
					
				final byte[] buffer = capture();
				final PcmSoundPacket packet = capture();
				new Stimulus() {{
					line = audio.bestAvailableTargetDataLine();
					stepper.step();
						line.open();
						line.start();
						
						line.read(buffer, 0, 640);
						buffer[0] = 10;
						
						subscriber.consume(packet);
						assertEquals(10, packet.payload.get(0));
						assertEquals(1, packet.sequence);
				}};
						
				new Stimulus() {{
					stepper.step();
						line.read(buffer, 0, 640);
						buffer[0] = 20;
						
						subscriber.consume(packet);
						assertEquals(20, packet.payload.get(0));
						assertEquals(2, packet.sequence);
				}};
				
				new Stimulus() {{
					stepper.step();	
						line.read(buffer, 0, 640);
						buffer[0] = 30;
						
						subscriber.consume(packet);
						assertEquals(30, packet.payload.get(0));
						assertEquals(3, packet.sequence);
				}};
				
				new Stimulus() {{
					mic.close();
				}};
				
				new Stimulus() {{
					assertEquals(false, stepper.step());
						line.close();
				}};
			}
		};
	}
}
