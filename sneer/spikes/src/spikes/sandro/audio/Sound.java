package spikes.sandro.audio;

public interface Sound {

	void captureAudio();

	void playAudio();

	void stopCapture(boolean stop);

}