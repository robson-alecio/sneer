package sneer.skin.colors;

import java.awt.Color;

import sneer.brickness.Brick;

@Brick
public interface Colors {

	Color hightContrast();
	Color moderateContrast();
	Color lowContrast();
	Color solid();
	Color invalid();

}
