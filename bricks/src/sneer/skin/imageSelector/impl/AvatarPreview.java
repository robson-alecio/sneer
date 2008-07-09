package sneer.skin.imageSelector.impl;

import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AvatarPreview extends JDialog {

	private static final long serialVersionUID = 1L;

	private final ImageDialog _imageDialog;

	JSlider top = new JSlider();
	JSlider botton = new JSlider();
	JSlider left = new JSlider();
	JSlider right = new JSlider();
	JSlider area = new JSlider();

	JCheckBox cropCheck = new JCheckBox("Crop Image");

	AvatarPreview(ImageDialog imageDialog) {
		_imageDialog = imageDialog;
		resizeAvatarPreview();

		JPanel panel = initPanelArea();

		panel.add(cropCheck);
		addCropListener();

		initSlider(top, panel);// , "Top Margin");
		initSlider(botton, panel);// , "Botton Margin");
		initSlider(left, panel);// , "Left Margin");
		initSlider(right, panel);// , "Right Margin");

		panel.add(new JSeparator());
		initAreaSlider(panel);

		addSizeCheck(panel, 24);
		addSizeCheck(panel, 32);
		addSizeCheck(panel, 48);
		addSizeCheck(panel, 64);
		addSizeCheck(panel, 128);
	}

	private JPanel initPanelArea() {
		JScrollPane scroll = new JScrollPane();
		getContentPane().add(scroll);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		scroll.getViewport().add(panel);
		return panel;
	}

	private void initAreaSlider(JPanel panel) {
		addSliderMouseWheelListener(area);
		area.setMaximumSize(new Dimension(200, 10));
		area.setMaximum(500);
		area.setBorder(new TitledBorder("Picker Area"));
		panel.add(area);
	}

	private void initSlider(final JSlider slider, JPanel panel) {
		// slider.setBorder(new TitledBorder(title));
		panel.add(slider);
		slider.setMaximumSize(new Dimension(200, 100));
		addSliderMouseWheelListener(slider);
		slider.setEnabled(false);
		slider.setMaximum(10000);
		slider.setMinimum(0);
		slider.setValue(1000);
	}

	private void addCropListener() {
		cropCheck.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent change) {
				top.setEnabled(cropCheck.isSelected());
				botton.setEnabled(cropCheck.isSelected());
				left.setEnabled(cropCheck.isSelected());
				right.setEnabled(cropCheck.isSelected());
			}
		});
	}
	
	private void addSliderMouseWheelListener(final JSlider slider) {
		slider.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int notches = e.getWheelRotation() * 5;
				int value = slider.getValue() - notches;
				if (value < 0)
					value = 0;
				slider.setValue(value);
				System.out.println(value);
			}
		});
	}

	private void addSizeCheck(JPanel panel, int size) {
		panel.add(new JSeparator());
		panel.add(new JCheckBox(size + "x" + size));
		MyLabel button = new MyLabel(size);
		panel.add(button);
	}

	void resizeAvatarPreview() {
		int x = 10 + _imageDialog.getLocation().x + _imageDialog.getWidth();
		int y = _imageDialog.getBounds().y;
		setBounds(x, y, 180, _imageDialog.getHeight());
	}
}

class MyLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	private final Dimension _size;

	MyLabel(int size){
		_size = new Dimension(size,size);
		setBorder(new BevelBorder(BevelBorder.LOWERED));
	}
	
	@Override
	public Dimension getPreferredSize() {
		return _size;
	}

	@Override
	public Dimension getMaximumSize() {
		return _size;
	}

	@Override
	public Dimension getMinimumSize() {
		return _size;
	}
}