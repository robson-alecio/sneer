package snapps.memorymeter.gui.impl;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import snapps.memorymeter.gui.MemoryMeterGui;
import sneer.kernel.container.Inject;
import sneer.skin.snappmanager.InstrumentManager;

public class MemoryMeterGuiImpl implements MemoryMeterGui {

	private JProgressBar _usedMemory;
	
	@Inject
	static private InstrumentManager _instruments;
	
	public MemoryMeterGuiImpl() {
		_instruments.registerInstrument(this);
	} 
	
	@Override
	public void init(Container container) {
		_usedMemory  = new JProgressBar(SwingConstants.HORIZONTAL);
		_usedMemory.setBorder(new TitledBorder("Used Memory"));
		container.setLayout(new BorderLayout());
		container.add(_usedMemory, BorderLayout.CENTER);
	}

	@Override
	public int defaultHeight() {
		return 40;
	}
	
	@Override
	public void setUsedMemory(int value) {
		if(_usedMemory.getMaximum()<value)
			_usedMemory.setMaximum(value);
		
		_usedMemory.setValue(value);
	}
}
