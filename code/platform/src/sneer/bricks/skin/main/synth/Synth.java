package sneer.bricks.skin.main.synth;

import javax.swing.JComponent;

import sneer.foundation.brickness.Brick;

@Brick
public interface Synth {

	void load(Class<?> resourceBase) ;
	void load(Object resourceBase) ;
	void attach(JComponent component);
	void attach(JComponent component, String synthName);
	Object getDefaultProperty(String key);
}
