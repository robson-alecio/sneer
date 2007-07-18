package sneer.apps.talk.audio;

public class LoopbackTest {

	public LoopbackTest() {
		SpeexSpeaker speaker = null;
		SpeexMicrophone microphone = null;
		try {
			speaker = new SpeexSpeaker();
			final SpeexSpeaker finalSpeaker = speaker;
			microphone = new SpeexMicrophone(new SpeexMicrophone.AudioConsumer() {
				public void audio(byte[][] contents) {
					finalSpeaker.sendAudio(contents, 1);

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print("waiting 60 seconds...");
		try {
			Thread.sleep(60000);
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
