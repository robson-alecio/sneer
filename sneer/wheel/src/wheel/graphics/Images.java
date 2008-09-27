package wheel.graphics;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class Images {

	public static Image getImage(URL url) {
		return Toolkit.getDefaultToolkit().getImage(url);
	}

}
