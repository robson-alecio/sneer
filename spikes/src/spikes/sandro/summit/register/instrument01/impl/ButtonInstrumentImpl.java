package spikes.sandro.summit.register.instrument01.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import sneer.bricks.skin.main.dashboard.InstrumentPanel;
import sneer.bricks.skin.main.instrumentregistry.InstrumentRegistry;
import spikes.sandro.summit.register.SimpleRegister;
import spikes.sandro.summit.register.instrument01.ButtonInstrument;

class ButtonInstrumentImpl implements ButtonInstrument {

	ButtonInstrumentImpl() {
		my(InstrumentRegistry.class).registerInstrument(this);
	}

	@Override public void init(InstrumentPanel container) {
		JButton btn = new JButton("change!");
		btn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
				my(SimpleRegister.class).setter().consume( "" + System.currentTimeMillis());
		}});
		container.contentPane().add(btn);
	}	

	@Override public String title() {
		return "Button Summit Test!";
	}
	
	@Override public int defaultHeight() {
		return 40;
	}
}
