package sneer.apps.talk.audio;

public class LoopbackTest {

	public LoopbackTest() {
		final SpeexSpeaker speaker = new SpeexSpeaker();
		SpeexMicrophone microphone = new SpeexMicrophone(new SpeexMicrophone.AudioCallback() {
			public void audio(byte[][] contents) {
				speaker.sendAudio(contents);
			}
		});
		try {
			speaker.init();
			microphone.init();
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
