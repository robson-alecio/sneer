package sneer.skin.main.synth.scroll.impl;

import javax.swing.JScrollPane;

import sneer.skin.main.synth.Synth;
import sneer.skin.main.synth.scroll.SynthScrolls;
import static sneer.commons.environments.Environments.my;

public class SynthScrollsImpl implements SynthScrolls {

	SynthScrollsImpl(){
		my(Synth.class).load(this.getClass());
	}
	
	@Override
	public JScrollPane create() {
		JScrollPane scroll = new JScrollPane();
		my(Synth.class).attach(scroll);
		return scroll;
	}

}
