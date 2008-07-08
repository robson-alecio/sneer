package sneer.skin.imageSelector.impl;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AvatarPreview extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private final ImageDialog _imageDialog;
	
	JSlider top = new JSlider();
	JSlider botton = new JSlider();
	JSlider left = new JSlider();
	JSlider right = new JSlider();
	
	JCheckBox cropCheck = new JCheckBox("Crop Image");

	AvatarPreview(ImageDialog imageDialog){
		_imageDialog = imageDialog;
		resizeAvatarPreview();
		
		SwingUtilities.invokeLater(
			new Runnable() {
				@Override
				public void run() {
					JScrollPane scroll = new JScrollPane();
					getContentPane().add(scroll);
					
					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
					scroll.getViewport().add(panel);
					
					panel.add(cropCheck);
					cropCheck.getModel().addChangeListener(
						new ChangeListener(){
							@Override
							public void stateChanged(ChangeEvent change) {
								top.setEnabled(cropCheck.isSelected());
								botton.setEnabled(cropCheck.isSelected());
								left.setEnabled(cropCheck.isSelected());
								right.setEnabled(cropCheck.isSelected());
							}
						}
					);
					
					
					initField(top);//, "Top Margin");
					initField(botton);//, "Botton Margin");
					initField(left);//, "Left Margin");
					initField(right);//, "Right Margin");
					
					panel.add(top, BorderLayout.NORTH);
					panel.add(botton, BorderLayout.SOUTH);
					panel.add(left, BorderLayout.EAST);
					panel.add(right, BorderLayout.WEST);
					
					panel.add(new JSeparator());
					
					addSizeCheck(panel, 24);
					addSizeCheck(panel, 32);
					addSizeCheck(panel, 48);
					addSizeCheck(panel, 64);
					addSizeCheck(panel, 128);
				}

				private void initField(JSlider slider) {
//					slider.setBorder(new TitledBorder(title));
					slider.setEnabled(false);
					slider.setMaximum(10000);
					slider.setMinimum(0);
					slider.setValue(1000);
				}

				private void addSizeCheck(JPanel panel, int size) {
					panel.add(new JCheckBox(size+"x"+size));
					MyLabel button = new MyLabel(size);
					panel.add(button);
					panel.add(new JSeparator());
				}
			}
		);			
	}

	void resizeAvatarPreview() {
		int x = 10 + _imageDialog.getLocation().x + _imageDialog.getWidth();
		int y = _imageDialog.getBounds().y;
		setBounds(x,y, 180,_imageDialog.getHeight());
	}
}

class MyLabel extends JLabel{

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