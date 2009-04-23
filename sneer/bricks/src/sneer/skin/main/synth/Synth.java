package sneer.skin.main.synth;

import javax.swing.JComponent;

import sneer.brickness.Brick;

@Brick
public interface Synth {

	void load(Class<?> resourceBase) ;
	void load(Object resourceBase) ;
	void attach(JComponent component);
	Object getDefaultProperty(String key);
}
