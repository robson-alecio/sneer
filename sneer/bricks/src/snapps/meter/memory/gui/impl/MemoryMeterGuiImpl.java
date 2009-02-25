package snapps.meter.memory.gui.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import snapps.meter.memory.gui.MemoryMeterGui;
import sneer.pulp.memory.MemoryMeter;
import sneer.skin.dashboard.InstrumentWindow;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.graphics.Images;
import wheel.lang.Functor;
import wheel.reactive.impl.Adapter;
import static wheel.lang.Environments.my;

class MemoryMeterGuiImpl implements MemoryMeterGui {

	private final InstrumentManager _instruments = my(InstrumentManager.class);
	
	private final ReactiveWidgetFactory _factory = my(ReactiveWidgetFactory.class);
	
	private final MemoryMeter _meter = my(MemoryMeter.class);
	
	JLabel _totalMemory =     new JLabel();
	TextWidget<JLabel> _maxUsedMemory;
	TextWidget<JLabel> _currentMemory;

	private static final Icon _memoryIconOn = load("recycleOn.png");
	private static final Icon _memoryIconOff = load("recycleOff.png");

	public MemoryMeterGuiImpl() {
		_instruments.registerInstrument(this);
	} 
	
	static Icon load(String resourceName){
		return new ImageIcon(Images.getImage(MemoryMeterGuiImpl.class.getResource(resourceName)));
	}
	
	@Override
	public void init(InstrumentWindow window) {
		Container container = window.contentPane();
		initGui(container);
	}

	private void initGui(Container container) {
		_totalMemory.setText("(Max " + _meter.maxMBs() + ")");
		initReactiveLabels();
		
		JPanel root = new JPanel();
		root.setOpaque(false);
		root.setLayout(new GridBagLayout());
		
		final JButton gc = new JButton(_memoryIconOff);
		gc.setMargin(new Insets(0,0,0,0));
		gc.setBorder(new EmptyBorder(0,0,0,0));
		gc.setOpaque(true);
		gc.setBackground(Color.WHITE);
		gc.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			System.gc();
		}});
		gc.addMouseListener(new MouseAdapter(){
			@Override public void mouseEntered(MouseEvent e) { gc.setIcon(_memoryIconOn);	}
			@Override public void mouseExited(MouseEvent e) { gc.setIcon(_memoryIconOff); }
		});
		
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
		return DEFAULT_HEIGHT;
	}

	@Override
	public String title() {
		return null;
	}
}