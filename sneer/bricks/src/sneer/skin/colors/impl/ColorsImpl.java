package sneer.skin.colors.impl;

import java.awt.Color;

import sneer.skin.colors.Colors;

public class ColorsImpl implements Colors {

	@Override public Color hightContrast() {				return Color.DARK_GRAY;  }
	@Override public Color lowContrast() { 				return Color.LIGHT_GRAY; }
	@Override public Color moderateContrast() { 		return new Color(190, 190, 190); }
	@Override public Color solid() {							return Color.WHITE; }
	@Override public Color invalid() {						return Color.YELLOW; }
	
	
	
}
