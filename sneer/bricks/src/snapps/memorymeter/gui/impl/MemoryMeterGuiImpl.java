package snapps.memorymeter.gui.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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

	private Icon _memoryIcon = new ImageIcon(this.getClass().getResource("recycle.png"));

	public MemoryMeterGuiImpl() {
		_instruments.registerInstrument(this);
	} 
	
	@Override
	public void init(Container container) {
		initGui(container);
	}

	private void initGui(Container container) {
		_totalMemory.setText("(Max " + _meter.maxMBs() + ")");
		initReactiveLabels();
		
		JPanel root = new JPanel();
		root.setOpaque(false);
		root.setLayout(new GridBagLayout());
		
		JButton gc = new JButton(_memoryIcon);
		gc.setMargin(new Insets(0,0,0,0));
		gc.setBorder(new EmptyBorder(0,0,0,0));
		gc.setOpaque(true);
		gc.setBackground(Color.WHITE);
		gc.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			System.gc();
		}});
		
		root.add(_currentMemory.getMainWidget(), new GridBagConstraints(0, 0, 1, 1, 1., 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 0, 2, 0), 0, 0));

		root.add(gc, new GridBagConstraints(1, 0, 1, 1, 1., 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(2, 0, 2, 0), 0, 0));
		
		root.add(_maxUsedMemory.getMainWidget(), new GridBagConstraints(2,0, 1, 1, 1., 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(2, 0, 2, 0), 0, 0));
		
		root.add(_totalMemory, new GridBagConstraints(3, 0, 1, 1, 1., 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 0, 2, 0), 0, 0));
		
		
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

	private void initReactiveLabels() {
		Functor<Integer, String> functor = new Functor<Integer, String>(){@Override public String evaluate(Integer value) {
			return "Peak: " + value;
		}};
		_maxUsedMemory = _factory.newLabel(new Adapter<Integer, String>(_meter.usedMBsPeak(), functor).output());
		
		functor = new Functor<Integer, String>(){@Override public String evaluate(Integer value) {
			return "MB Used: " + value;
		}};
		_currentMemory = _factory.newLabel(new Adapter<Integer, String>(_meter.usedMBs(), functor).output());
	}

	@Override
	public int defaultHeight() {
		return ANY_HEIGHT;
	}
}