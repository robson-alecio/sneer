package spikes.sandro.audio;

public class MainJava {

	public static void main(String[] args) {
		new LoopBackGui(new JavaSoundImpl()).setVisible(true);
	}

}
