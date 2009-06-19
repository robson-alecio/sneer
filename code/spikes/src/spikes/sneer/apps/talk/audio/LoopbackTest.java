package spikes.sneer.apps.talk.audio;


public class LoopbackTest {

	public LoopbackTest() {
		SpeexSpeaker speaker = null;
		SpeexMicrophone microphone = null;
		try {
			speaker = new SpeexSpeaker();
			final SpeexSpeaker finalSpeaker = speaker;
			microphone = new SpeexMicrophone(new SpeexMicrophone.AudioConsumer() {
				public void audio(byte[][] contents) {
					
					finalSpeaker.sendAudio(contents);

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n\nTalk into the mic now. Your own voice will be played.");
		System.out.print("Closing in 15 seconds...");
		try {
			Thread.sleep(15000);
		} catch (Exception e) {
		}
		System.out.print("closing...");
		microphone.close();
		speaker.close();
		System.out.println("Done.");
	}

	public static void main(String[] args) {
		new LoopbackTest();
	}
}
