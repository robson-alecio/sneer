package snapps.memorymeter.gui.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import snapps.memorymeter.gui.MemoryMeterGui;
import sneer.kernel.container.Inject;
import sneer.pulp.memory.MemoryMeter;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Functor;
import wheel.reactive.impl.Adapter;

public class MemoryMeterGuiImpl implements MemoryMeterGui {

	@Inject
	static private InstrumentManager _instruments;
	
	@Inject
	static private ReactiveWidgetFactory _factory;
	
	@Inject
	static private MemoryMeter _meter;
	
	JLabel _totalMemory =     new JLabel();
	TextWidget<JLabel> _maxUsedMemory;
	TextWidget<JLabel> _currentMemory;

	private Icon _memoryIcon = new ImageIcon(this.getClass().getResource("memory.png"));

	public MemoryMeterGuiImpl() {
		_instruments.registerInstrument(this);
	} 
	
	@Override
	public void init(Container container) {
		initGui(container);
	}

	private void initGui(Container container) {
		_totalMemory.setText("Total: " + _meter.maxMBs() + "Mb");
		initReactiveLabels();
		
		JPanel root = new JPanel();
		root.setBorder(new TitledBorder(new EmptyBorder(5,5,2,2), getName()));	
		root.setOpaque(false);
		root.setLayout(new GridBagLayout());
		
		root.add(_totalMemory, new GridBagConstraints(1, 0, 1, 1, 1., 0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		_totalMemory.setIcon(_memoryIcon);

		root.add(_maxUsedMemory.getMainWidget(), new GridBagConstraints(1, 1, 1, 1, 1., 0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		_maxUsedMemory.getMainWidget().setIcon(_memoryIcon);

		root.add(_currentMemory.getMainWidget(), new GridBagConstraints(1, 2, 1, 1, 1., 0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		_currentMemory.getMainWidget().setIcon(_memoryIcon);
		
		changeLabelFont(_totalMemory);
		changeLabelFont(_maxUsedMemory.getMainWidget());
		changeLabelFont(_currentMemory.getMainWidget());
		
		container.setBackground(Color.WHITE);
		container.setLayout(new BorderLayout());
		container.add(root, BorderLayout.CENTER);
	}

	private void changeLabelFont(JLabel label) {
		label.setFont(label.getFont().deriveFont(Font.ITALIC));
	}

	private String getName() {
		return "Memory Meter";
	}

	private void initReactiveLabels() {
		Functor<Integer, String> functor = new Functor<Integer, String>(){@Override public String evaluate(Integer value) {
			return "Max.Used: " + value + "Mb";
		}};
		_maxUsedMemory = _factory.newLabel(new Adapter<Integer, String>(_meter.usedMBsPeak(), functor).output());
		
		functor = new Functor<Integer, String>(){@Override public String evaluate(Integer value) {
			return "Current: " + value + "Mb";
		}};
		_currentMemory = _factory.newLabel(new Adapter<Integer, String>(_meter.usedMBs(), functor).output());
	}

	@Override
	public int defaultHeight() {
		return ANY_HEIGHT;
	}
}