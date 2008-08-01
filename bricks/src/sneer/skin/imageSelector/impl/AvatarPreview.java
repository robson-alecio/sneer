package sneer.skin.imageSelector.impl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.TitledBorder;
import javax.swing.Icon;
import javax.swing.JLabel;

public class AvatarPreview extends JDialog {

	public static final int _WIDTH = 180;
	private static final long serialVersionUID = 1L;
	private int _counter = 0;

	final ImageDialog _imageDialog;

	JSlider _top = new JSlider();
	JSlider _botton = new JSlider();
	JSlider _left = new JSlider();
	JSlider _right = new JSlider();
	JSlider _area = new JSlider();

	JCheckBox _cropCheck = new JCheckBox("Crop Image");
	List<AvatarIcon> _avatarList = new ArrayList<AvatarIcon>();

	AvatarPreview(ImageDialog imageDialog) {
		_imageDialog = imageDialog;
		resizeAvatarPreview();

		JPanel panel = initPrincipalPanel();
		JInternalFrame iwin;
		
		iwin = initInternalFrame(panel,"Picture");
		addGridItem(iwin.getContentPane(),_cropCheck);
		
		initSlider(_left, iwin.getContentPane());
		initSlider(_right, iwin.getContentPane());
		initSlider(_botton, iwin.getContentPane());
		initSlider(_top, iwin.getContentPane());
		
		_right.setInverted(true);
		_botton.setInverted(true);		

		iwin = initInternalFrame(panel,"Keyhole");

		addAvatarIcon(iwin.getContentPane(), 32);
		addAvatarIcon(iwin.getContentPane(), 48);
		addAvatarIcon(iwin.getContentPane(), 64);
		addAvatarIcon(iwin.getContentPane(), 128);
		initKeyholeSizeSlider(iwin.getContentPane());

		addCropListener();
	}

	void resizeAvatarPreview() {
		int x = 10 + _imageDialog.getLocation().x + _imageDialog.getWidth();
		int y = _imageDialog.getBounds().y;
		setBounds(x, y, _WIDTH, _imageDialog.getHeight());
	}
	
	int getRightCropLocation() {
		return (int)(_imageDialog.getWidth()*(1-getDoubleValue(_right.getValue())));
	}

	int getLeftCropLocation() {
		return (int)(_imageDialog.getWidth()*getDoubleValue(_left.getValue()));
	}

	int getBottonCropLocation() {
		return (int)(_imageDialog.getHeight()*(1-getDoubleValue(_botton.getValue())));
	}

	int getTopCropLocation() {
		return (int)(_imageDialog.getHeight()*getDoubleValue(_top.getValue()));
	}	
	
	private double getDoubleValue(double value) {
		if(_cropCheck.isSelected())
			return (value/10000);
		return 0;
	}	
	private JInternalFrame initInternalFrame(JPanel panel, String title) {
		_counter = 0;
		final JInternalFrame iwin = new JInternalFrame(title);
		panel.add(iwin);
		iwin.getContentPane().setLayout(new GridBagLayout());
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

	private void initKeyholeSizeSlider(Container container) {
		initSlider(_area, container);
		_area.setMaximum(500);
		_area.setMinimum(24);
		_area.setEnabled(true);
		_area.addMouseListener(
			new MouseAdapter(){
				@Override
				public void mouseEntered(MouseEvent arg0) {
					_imageDialog._keyhole.setTrueLocation(10,10);
					_imageDialog._keyhole.setVisible(true);
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
					_imageDialog._keyhole.setVisible(false);
				}
			}
		);
		_area.setToolTipText("Tip: You can use mouse wheel to set the keyhole size.");
	}

	private void initSlider(JSlider slider, Container container) {
		addGridItem(container, slider);
		slider.setSize(new Dimension(128, 20));
		slider.setPreferredSize(new Dimension(128, 20));
		addSliderMouseWheelListener(slider);
		slider.setEnabled(false);
		slider.setMaximum(10000);
		slider.setMinimum(0);
		slider.setValue(1000);
	}

	private void addCropListener() {
		_cropCheck.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent change) {
				_top.setEnabled(_cropCheck.isSelected());
				_botton.setEnabled(_cropCheck.isSelected());
				_left.setEnabled(_cropCheck.isSelected());
				_right.setEnabled(_cropCheck.isSelected());
				_imageDialog._keyhole.setTrueLocation();
			}
		});
	}
	
	private void addSliderMouseWheelListener(final JSlider slider) {
		slider.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int notches = e.getWheelRotation() * 10;
				int value = slider.getValue() - notches;
				if (value < 0)
					value = 0;
				slider.setValue(value);
			}
		});
	}

	private void addAvatarIcon(Container container, int size) {
		AvatarIcon label = new AvatarIcon(size);
		_avatarList.add(label);
		addGridItem(container, label);
	}
	
	private void addGridItem(Container container, Component componet) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = _counter;
		c.anchor = GridBagConstraints.NORTHWEST;
		_counter++;
		container.add(componet,c);
	}	

}

class AvatarIcon extends JLabel {
	private static final long serialVersionUID = 1L;
	Dimension _size;
	boolean useSize = true;

	AvatarIcon(int size) {
		_size = new Dimension(size, size);
		setBorder(new TitledBorder(""));
	}

	@Override
	public void setIcon(Icon icon) {
		super.setIcon(icon);
		useSize = false;
	}

	@Override
	public Dimension getPreferredSize() {
		if (useSize)
			return _size;
		return super.getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {
		if (useSize)
			return _size;
		return super.getMaximumSize();
	}

	@Override
	public Dimension getMinimumSize() {
		if (useSize)
			return _size;
		return super.getMinimumSize();
	}
}