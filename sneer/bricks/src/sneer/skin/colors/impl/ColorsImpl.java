package sneer.skin.colors.impl;

import java.awt.Color;

import sneer.skin.colors.Colors;

public class ColorsImpl implements Colors {

	@Override public Color hightContrast() {				return Color.DARK_GRAY;  }
	@Override public Color moderateContrast() { 		return Color.LIGHT_GRAY; }
	@Override public Color lowContrast() { 				return new Color(230, 230, 230); }
	@Override public Color solid() {							return Color.WHITE; }
	@Override public Color invalid() {						return new Color(255, 255, 200); }
}
