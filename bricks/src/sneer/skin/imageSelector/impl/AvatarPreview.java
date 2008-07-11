package sneer.skin.imageSelector.impl;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AvatarPreview extends JDialog {

	public static final int _WIDTH = 180;

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

		JPanel panel = initPrincipalPanel();
		JInternalFrame iwin;
		
		iwin = initInternalFrame(panel,"Picture");
		iwin.add(cropCheck);
		
		initSlider(left, iwin.getContentPane());
		initSlider(right, iwin.getContentPane());
		initSlider(botton, iwin.getContentPane());
		initSlider(top, iwin.getContentPane());
		
		right.setInverted(true);
		botton.setInverted(true);		
		iwin = initInternalFrame(panel,"Keyhole Size");
		initAreaSlider(iwin.getContentPane());

		iwin = initInternalFrame(panel,"Keyhole Preview");

		addSizeCheck(iwin.getContentPane(), 24);
		addSizeCheck(iwin.getContentPane(), 32);
		addSizeCheck(iwin.getContentPane(), 48);
		addSizeCheck(iwin.getContentPane(), 64);
		addSizeCheck(iwin.getContentPane(), 128);

		addCropListener();
	}

	private JInternalFrame initInternalFrame(JPanel panel, String title) {
		final JInternalFrame iwin = new JInternalFrame(title);
		panel.add(iwin);
		iwin.getContentPane().setLayout(new BoxLayout(iwin.getContentPane(), BoxLayout.PAGE_AXIS));
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						iwin.setVisible(true);
					}
				}
		);	
		return iwin;
	}

	private JPanel initPrincipalPanel() {
		JScrollPane scroll = new JScrollPane();
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(scroll);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		scroll.getViewport().add(panel);
		return panel;
	}

	private void initAreaSlider(Container container) {
		initSlider(area, container);
		area.setMaximum(500);
		area.setMinimum(24);
//		area.setMajorTickSpacing(area.getMaximum()/5);
//		area.setMinorTickSpacing(area.getMaximum()/10);
		area.setEnabled(true);
	}

	private void initSlider(final JSlider slider, Container container) {
	    container.add(slider);
		slider.setMaximumSize(new Dimension(200, 100));
		addSliderMouseWheelListener(slider);
		slider.setEnabled(false);
		slider.setMaximum(10000);
		slider.setMinimum(0);
		slider.setValue(1000);
//		slider.setPaintTicks(true);
//		slider.setPaintLabels(false);
//		slider.setMajorTickSpacing(slider.getMaximum()/5);
//		slider.setMinorTickSpacing(slider.getMaximum()/10);
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

	private void addSizeCheck(Container container, int size) {
		container.add(new JSeparator());
		container.add(new JCheckBox(size + "x" + size));
		MyLabel button = new MyLabel(size);
		container.add(button);
	}

	void resizeAvatarPreview() {
		int x = 10 + _imageDialog.getLocation().x + _imageDialog.getWidth();
		int y = _imageDialog.getBounds().y;
		setBounds(x, y, _WIDTH, _imageDialog.getHeight());
	}
}

class MyLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	private final Dimension _size;

	MyLabel(int size){
		_size = new Dimension(size,size);
		setBorder(new TitledBorder(""));
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