package sneer.bricks.skin.main.synth;

import javax.swing.JComponent;

import sneer.foundation.brickness.Brick;

@Brick
public interface Synth {

	void load(Class<?> resourceBase) ;
	void loadForWussies(Class<?> resourceBase) ;
	void notInGuiThreadLoad(Class<?> resourceBase);
	
	void attach(JComponent component);
	void attach(JComponent component, String synthName);
	void attachForWussies(JComponent component);
	void attachForWussies(JComponent component, String synthName);
	void notInGuiThreadAttach(JComponent component);
	void notInGuiThreadAttach(JComponent component, String synthName);

	Object getDefaultProperty(String key);
}
