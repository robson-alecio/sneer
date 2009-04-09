package snapps.meter.bandwidth.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import snapps.meter.bandwidth.gui.BandwidthMeterGui;
import sneer.commons.lang.Functor;
import sneer.pulp.bandwidth.BandwidthCounter;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.skin.colors.Colors;
import sneer.skin.dashboard.InstrumentWindow;
import sneer.skin.snappmanager.InstrumentRegistry;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.graphics.Images;

class BandwidthMeterGuiImpl implements BandwidthMeterGui {

	private TextWidget<JLabel> _uploadField;
	private TextWidget<JLabel> _downloadField;

	private static final Icon _upload = load("upload.png");
	private static final Icon _download = load("download.png");

	public BandwidthMeterGuiImpl() {
		my(InstrumentRegistry.class).registerInstrument(this);
	} 
	
	class MaxHolderFunctor implements Functor<Integer, String>{
		int _maxValue = 0;
		
		@Override public String evaluate(Integer value) {
			if(_maxValue<value) _maxValue=value;
			return value + " (" + _maxValue + ")";
		}
	}
	
	static Icon load(String resourceName){
		return new ImageIcon(Images.getImage(BandwidthMeterGuiImpl.class.getResource(resourceName)));
	}	
	
	@Override
	public void init(InstrumentWindow window) {
		initGui(window.contentPane());
	}

	private void initGui(Container container) {
		ReactiveWidgetFactory factory = my(ReactiveWidgetFactory.class);
		
		Signal<Integer> upload = my(BandwidthCounter.class).uploadSpeed();
		Signal<Integer> download = my(BandwidthCounter.class).downloadSpeed();
		_uploadField = factory.newLabel(maxHolder(upload));
		_downloadField = factory.newLabel(maxHolder(download));
		JLabel lbUpload = _uploadField.getMainWidget();
		JLabel lbDownload = _downloadField.getMainWidget();
		
		Dimension preferedSize = new Dimension(70,16);
		lbUpload.setPreferredSize(preferedSize);
		lbDownload.setPreferredSize(preferedSize);
		
		JPanel root = new JPanel();
		root.setOpaque(false);
		root.setLayout(new FlowLayout());
		
		JLabel label = new JLabel("kB/s(Peak)");
		root.add(label);
		root.add(lbUpload);
		root.add(lbDownload);
		
		changeLabelFont(label);
		changeLabelFont(lbUpload);
		changeLabelFont(lbDownload);
		
		lbUpload.setIcon(_upload);
		lbDownload.setIcon(_download);
		
		container.setBackground(my(Colors.class).solid());
		container.setLayout(new BorderLayout());
		container.add(root, BorderLayout.CENTER);
	}

	private Signal<String> maxHolder(Signal<Integer> input) {
		return my(Signals.class).adapt(input, new MaxHolderFunctor());
	}

	private void changeLabelFont(JLabel label) {
		label.setFont(label.getFont().deriveFont(Font.ITALIC));
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