package sneer.skin.main.synth;

import javax.swing.JComponent;

public interface Synth {

	void load(Class<?> resourceBase) ;
	void attach(JComponent component);
}
