package snapps.meter.memory.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import snapps.meter.memory.gui.MemoryMeterGui;
import sneer.commons.lang.Functor;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.memory.MemoryMeter;
import sneer.pulp.reactive.Signals;
import sneer.skin.main.dashboard.InstrumentPanel;
import sneer.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.skin.main.synth.Synth;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;

class MemoryMeterGuiImpl implements MemoryMeterGui {

	private final InstrumentRegistry _instruments = my(InstrumentRegistry.class);
	private final ReactiveWidgetFactory _factory = my(ReactiveWidgetFactory.class);
	private final MemoryMeter _meter = my(MemoryMeter.class);
	private final JButton gc = new JButton();
	private final Synth _synth = my(Synth.class);
	
	JLabel _totalMemory = new JLabel();
	
	private final TextWidget<JLabel> _maxUsedMemory;{//Fix Use GUI Nature
		final Object ref[] = new Object[1];
		my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
			ref[0] = _factory.newLabel(my(Signals.class).adapt(_meter.usedMBsPeak(), 
					new Functor<Integer, String>(){@Override public String evaluate(Integer value) {
						return "Peak: " + value;
					}}
			), "MaxUsedMemoryLabel");
		}});
		_maxUsedMemory = (TextWidget<JLabel>) ref[0];
	}
	
	private final TextWidget<JLabel> _currentMemory; {//Fix Use GUI Nature
		final Object ref[] = new Object[1];
		my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
			ref[0]  = _factory.newLabel(my(Signals.class).adapt(_meter.usedMBs(), 
					new Functor<Integer, String>(){@Override public String evaluate(Integer value) {
						return "MB Used: " + value;
					}}
			), "CurrentMemoryLabel");
		}});
		_currentMemory = (TextWidget<JLabel>) ref[0];
	}

	public MemoryMeterGuiImpl() {
		_instruments.registerInstrument(this);
		initSynth();
	} 

	private void initSynth() {
		_synth.load(this);
		
		gc.setName("GCButton");
		_totalMemory.setName("TotalMemoryLabel");
		
		_synth.attach(gc);
		_synth.attach(_totalMemory);
	}
	
	@Override
	public void init(InstrumentPanel window) {
		JComponent container = (JComponent) window.contentPane();
		
		_totalMemory.setText("(Max " + _meter.maxMBs() + ")");
		container.setLayout(new GridBagLayout());

		gc.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			System.gc();
		}});
		
		container.add(_currentMemory.getMainWidget(), new GridBagConstraints(0, 0, 1, 1, 1., 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 0, 2, 0), 0, 0));

		container.add(gc, new GridBagConstraints(1, 0, 1, 1, 1., 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(2, 0, 2, 0), 0, 0));
		
		container.add(_maxUsedMemory.getMainWidget(), new GridBagConstraints(2,0, 1, 1, 1., 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(2, 0, 2, 0), 0, 0));
		
		container.add(_totalMemory, new GridBagConstraints(3, 0, 1, 1, 1., 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 0, 2, 0), 0, 0));
	}

	@Override
	public int defaultHeight() {
		return DEFAULT_HEIGHT;
	}

	@Override
	public String title() {
		return "Memory Meter";
	}
}