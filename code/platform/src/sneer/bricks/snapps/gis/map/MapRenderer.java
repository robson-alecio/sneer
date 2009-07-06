package sneer.bricks.snapps.gis.map;

import java.awt.Image;

import sneer.bricks.snapps.gis.location.Location;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface MapRenderer {

	void render(Consumer<Image> receiver, Location location);
	void render(Consumer<Image> receiver, Location location, int zoom);
}
