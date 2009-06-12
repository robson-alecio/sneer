package sneer.bricks.snapps.gis.map;

import java.awt.Image;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.snapps.gis.location.Location;
import sneer.foundation.brickness.Brick;

@Brick
public interface MapRenderer {

	Signal<Image> render(Location location);
}
