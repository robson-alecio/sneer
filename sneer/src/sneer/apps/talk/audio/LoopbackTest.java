package sneer.apps.talk.audio;

public class LoopbackTest {

	public LoopbackTest() {
		final SpeexSpeaker speaker = new SpeexSpeaker();
		SpeexMicrophone microphone = new SpeexMicrophone(new SpeexMicrophone.AudioCallback() {
			public void audio(byte[] buffer, int offset, int length) {
				speaker.sendAudio(buffer, length);
			}
		});
		try {
			speaker.init();
			microphone.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print("waiting 10 seconds...");
		try {
			Thread.sleep(10000);
		} catch (Exception e) {
		}
		System.out.print("closing...");
		microphone.close();
		speaker.close();
		System.out.println("Done.");
	}

	public static void main(String[] args) {
		LoopbackTest test = new LoopbackTest();
	}
}
