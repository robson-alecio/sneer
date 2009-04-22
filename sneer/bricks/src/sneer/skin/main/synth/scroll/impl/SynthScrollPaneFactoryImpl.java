package sneer.skin.main.synth.scroll.impl;

import javax.swing.JScrollPane;

import sneer.skin.main.synth.Synth;
import sneer.skin.main.synth.scroll.SynthScrollPaneFactory;
import static sneer.commons.environments.Environments.my;

public class SynthScrollPaneFactoryImpl implements SynthScrollPaneFactory {

	SynthScrollPaneFactoryImpl(){
		my(Synth.class).load(this.getClass());
	}
	
	@Override
	public JScrollPane create() {
		JScrollPane scroll = new JScrollPane();
		my(Synth.class).attach(scroll);
		return scroll;
	}

}
