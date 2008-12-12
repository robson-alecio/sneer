package snapps.meter.bandwidth.gui.impl;

import static wheel.lang.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import snapps.meter.bandwidth.gui.BandwidthMeterGui;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Functor;
import wheel.reactive.Signal;
import wheel.reactive.impl.Adapter;
import wheel.reactive.impl.Constant;

public class BandwidthMeterGuiImpl implements BandwidthMeterGui {

	private TextWidget<JLabel> _uploadField;
	private TextWidget<JLabel> _downloadField;

	public BandwidthMeterGuiImpl() {
		my(InstrumentManager.class).registerInstrument(this);
	} 
	
	class MaxHolderFunctor implements Functor<Integer, String>{
		int _maxValue = 0;
		
		@Override public String evaluate(Integer value) {
			if(_maxValue<value) _maxValue=value;
			return value.toString() + " (" + _maxValue + ")";
		}
	}
	
	static Icon load(String resourceName){
		try {
			return new ImageIcon(ImageIO.read(BandwidthMeterGuiImpl.class.getResource(resourceName)));
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e);
		}
	}
	
	@Override
	public void init(Container container) {
		initGui(container);
	}

	private void initGui(Container container) {
		ReactiveWidgetFactory factory = my(ReactiveWidgetFactory.class);
		
		//kB/s(Peak)   v 12(31)    ^ 32(41)
		Signal<Integer> upload = new Constant<Integer>(10); //Implement - attach in meter
		Signal<Integer> download = new Constant<Integer>(20); //Implement - attach in meter
		_uploadField = factory.newLabel(new Adapter<Integer, String>(upload, new MaxHolderFunctor()).output());
		_downloadField = factory.newLabel(new Adapter<Integer, String>(download, new MaxHolderFunctor()).output());
		
		JPanel root = new JPanel();
		root.setOpaque(false);
		root.setLayout(new FlowLayout());
		
		JLabel label = new JLabel("kB/s(Peak)");
		root.add(label);
		root.add(_uploadField.getMainWidget());
		root.add(_downloadField.getMainWidget());
		
		changeLabelFont(label);
		changeLabelFont(_uploadField.getMainWidget());
		changeLabelFont(_downloadField.getMainWidget());
		
		container.setBackground(Color.WHITE);
		container.setLayout(new BorderLayout());
		container.add(root, BorderLayout.CENTER);
	}

	private void changeLabelFont(JLabel label) {
		label.setFont(label.getFont().deriveFont(Font.ITALIC));
	}

	@Override
	public int defaultHeight() {
		return ANY_HEIGHT;
	}
}