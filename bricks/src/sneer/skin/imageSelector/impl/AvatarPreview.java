package sneer.skin.imageSelector.impl;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
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

	JSlider _top = new JSlider();
	JSlider _botton = new JSlider();
	JSlider _left = new JSlider();
	JSlider _right = new JSlider();
	JSlider _area = new JSlider();

	JCheckBox _cropCheck = new JCheckBox("Crop Image");
	List<MyLabel> _avatarList = new ArrayList<MyLabel>();

	AvatarPreview(ImageDialog imageDialog) {
		_imageDialog = imageDialog;
		resizeAvatarPreview();

		JPanel panel = initPrincipalPanel();
		JInternalFrame iwin;
		
		iwin = initInternalFrame(panel,"Picture");
		iwin.add(_cropCheck);
		
		initSlider(_left, iwin.getContentPane());
		initSlider(_right, iwin.getContentPane());
		initSlider(_botton, iwin.getContentPane());
		initSlider(_top, iwin.getContentPane());
		
		_right.setInverted(true);
		_botton.setInverted(true);		
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
		iwin.getContentPane().setLayout(new BoxLayout(iwin.getContentPane(), BoxLayout.Y_AXIS));
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
		initSlider(_area, container);
		_area.setMaximum(500);
		_area.setMinimum(24);
		_area.setEnabled(true);
	}

	private void initSlider(final JSlider slider, Container container) {
	    container.add(slider);
		slider.setMaximumSize(new Dimension(200, 100));
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
		MyLabel label = new MyLabel(size);
		container.add(label);
		_avatarList.add(label);
	}

	void resizeAvatarPreview() {
		int x = 10 + _imageDialog.getLocation().x + _imageDialog.getWidth();
		int y = _imageDialog.getBounds().y;
		setBounds(x, y, _WIDTH, _imageDialog.getHeight());
	}
}

class MyLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	Dimension _size;
	boolean useSize = true;

	MyLabel(int size){
		_size = new Dimension(size,size);
		setBorder(new TitledBorder(""));
	}
	
	@Override
	public void setIcon(Icon icon) {
		super.setIcon(icon);
		useSize = false;
	}
	
	@Override
	public Dimension getPreferredSize() {
		if(useSize)
			return _size;
		return super.getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {
		if(useSize)
			return _size;
		return super.getMaximumSize();
	}

	@Override
	public Dimension getMinimumSize() {
		if(useSize)
			return _size;
		return super.getMinimumSize();
	}
}