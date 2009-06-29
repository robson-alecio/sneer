package sneer.bricks.skin.main.synth.scroll.impl;

import javax.swing.JScrollPane;

import sneer.bricks.skin.main.synth.Synth;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import static sneer.foundation.environments.Environments.my;

public class SynthScrollsImpl implements SynthScrolls {

	SynthScrollsImpl(){
		my(Synth.class).loadForWussies(this.getClass());
	}
	
	@Override
	public JScrollPane create() {
		JScrollPane scroll = new JScrollPane();
		my(Synth.class).attachForWussies(scroll);
		return scroll;
	}

}
