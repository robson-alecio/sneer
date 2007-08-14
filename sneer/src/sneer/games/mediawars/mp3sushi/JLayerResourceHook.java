package sneer.games.mediawars.mp3sushi;

import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerHook;

public class JLayerResourceHook implements JavaLayerHook{

	public InputStream getResourceAsStream(String name) {
		return JLayerResourceHook.class.getResourceAsStream("/sneer/games/mediawars/mp3sushi/"+name);
	}

}
