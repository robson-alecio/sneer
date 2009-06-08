package spikes.sandro.summit.register.instrument02.impl;

import static sneer.commons.environments.Environments.my;

import javax.swing.JTextField;

import sneer.skin.main.dashboard.InstrumentPanel;
import sneer.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import spikes.sandro.summit.register.SimpleRegister;
import spikes.sandro.summit.register.instrument02.TextInstrument;

class TextInstrumentImpl implements TextInstrument {

	TextInstrumentImpl() {
		my(InstrumentRegistry.class).registerInstrument(this);
	}

	@Override public void init(InstrumentPanel container) {
		SimpleRegister sr = my(SimpleRegister.class);
		
//		TextWidget<JLabel> label = my(ReactiveWidgetFactory.class).newLabel(sr.output());
		TextWidget<JTextField> label = my(ReactiveWidgetFactory.class).newEditableLabel(sr.output(), sr.setter());
		
		container.contentPane().add( label.getComponent() );
	}	

	@Override public String title() {
		return "Text Summit Test!";
	}
	
	@Override public int defaultHeight() {
		return 40;
	}
}
