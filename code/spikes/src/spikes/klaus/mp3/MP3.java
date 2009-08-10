package spikes.klaus.mp3;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
 
public class MP3 {
 
	public static void main(String[] args) throws FileNotFoundException, JavaLayerException {
		String path = "C:/Documents and Settings/User1/Desktop/mp3/musica/Avril Lavigne - Girlfriend.mp3";
		File mp3File = new File(path);
		MP3 musica = new MP3(mp3File);
		musica.play();
	}

	
	private File mp3;
 
	private Player player;
 
	public MP3(File mp3) {
		this.mp3 = mp3;
	}
 
	@SuppressWarnings("deprecation")
	public void play() throws FileNotFoundException, JavaLayerException {
            FileInputStream fis     = new FileInputStream(mp3);
            //BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(fis);
            
            
            final Thread playerThread = new Thread() { @Override public void run() {
                System.out.println("Tocando!");
                try {
					player.play();
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
                System.out.println("Terminado!");
			}};
			
			playerThread.start();
            
			while (true) {
				sleepForABit();
				playerThread.suspend();
				sleepForABit();
				playerThread.resume();
			}
     }

	private void sleepForABit() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}