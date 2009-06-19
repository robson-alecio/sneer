package spikes.sandro.cellrenderer;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import sneer.bricks.hardware.gui.images.Images;
import spikes.wheel.testutil.MemorySentinel;

public class ComplexRenderingSample {

	static final Image _me = getImage();
	
	static Object elements[][] = {
			{ new Font("Helvetica", Font.PLAIN, 20), Color.red, new ImageIcon(_me), "Help" },
			{ new Font("TimesRoman", Font.BOLD, 14), Color.blue, new ImageIcon(_me), "Me" },
			{ new Font("Courier", Font.ITALIC, 18), Color.green,	new ImageIcon(_me), "I'm" },
			{ new Font("Helvetica", Font.BOLD | Font.ITALIC, 12), Color.gray,	new ImageIcon(_me), "Trapped" },
			{ new Font("TimesRoman", Font.PLAIN, 32), Color.pink, new ImageIcon(_me), "Inside" },
			{ new Font("Courier", Font.BOLD, 16), Color.yellow, new ImageIcon(_me), "This" },
			{ new Font("Helvetica", Font.ITALIC, 8), Color.darkGray,	new ImageIcon(_me), "Computer" } };

	private static Image getImage(){
		return my(Images.class).getImage(ComplexRenderingSample.class.getResource("me.png"));
	}	
	
	public static void main(String args[]) {
		MemorySentinel.startLoggingSignificantMemoryUsageChanges();
		initGui();
	}

	private static void initGui() {
		JFrame frame = new JFrame("Complex Renderer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();

		final DefaultListModel model = new DefaultListModel();
		JList jlist = new JList(model);
		ListCellRenderer renderer = new ComplexCellRenderer();
		jlist.setCellRenderer(renderer);
		JScrollPane scrollPane = new JScrollPane(jlist);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JButton btn = new JButton("Add...");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (Object[] element : elements) {
					model.addElement(element);
				}
			}
		});

		contentPane.add(btn, BorderLayout.SOUTH);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
}

class ComplexCellRenderer implements ListCellRenderer {
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Font theFont = null;
		Color theForeground = null;
		Icon theIcon = null;
		String theText = null;

		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);

		Object values[] = (Object[]) value;
		theFont = (Font) values[0];
		theForeground = (Color) values[1];
		theIcon = (Icon) values[2];
		theText = (String) values[3];
		
		if (!isSelected) {
			renderer.setForeground(theForeground);
		}
		if (theIcon != null) {
			renderer.setIcon(theIcon);
		}
		renderer.setText(theText);
		renderer.setFont(theFont);
		return renderer;
	}
}