package spikes.bamboo.mocotoh;

import static org.junit.Assert.assertEquals;

import javax.sound.sampled.TargetDataLine;

import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.bricks.hardware.cpu.threads.Stepper;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.skin.audio.PcmSoundPacket;
import sneer.bricks.skin.audio.kernel.Audio;
import sneer.bricks.skin.audio.mic.Mic;
import sneer.foundation.lang.Consumer;

@RunWith(Mocotoh.class)
public class NewStyleMicTest {

	@Test
	public void test() throws Exception {
		
		new TestSpec() {
			
			final Mic mic = my(Mic.class); //Why not simply "my()" without parameter, just like mock()?
			final TupleSpace tuples = my(TupleSpace.class);
			final Threads threads = mock();
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
					line = audio.tryToOpenCaptureLine();
					stepper.step();
						line.open();
						line.start();
						
						line.read(buffer, 0, 640);
						buffer[0] = 10;
						
						subscriber.consume(packet);
						assertEquals(10, packet.payload.get(0));
//						assertEquals(1, packet.sequence);
				}};
						
				new Stimulus() {{
					stepper.step();
						line.read(buffer, 0, 640);
						buffer[0] = 20;
						
						subscriber.consume(packet);
						assertEquals(20, packet.payload.get(0));
//						assertEquals(2, packet.sequence);
				}};
				
				new Stimulus() {{
					stepper.step();	
						line.read(buffer, 0, 640);
						buffer[0] = 30;
						
						subscriber.consume(packet);
						assertEquals(30, packet.payload.get(0));
//						assertEquals(3, packet.sequence);
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
