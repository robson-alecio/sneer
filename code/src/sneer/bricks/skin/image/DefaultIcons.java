package sneer.bricks.skin.image;

import java.net.URL;

public interface DefaultIcons {

	String logo16x16 = "logo16x16.png";
	String logo32x32 = "logo32x32.png";
	String logoTray	 = "logoTray.png";
	
	URL getImageUrl(String name);

}