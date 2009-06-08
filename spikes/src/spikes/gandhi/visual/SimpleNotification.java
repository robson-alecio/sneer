package spikes.gandhi.visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;

import javax.swing.JEditorPane;
import javax.swing.JWindow;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class SimpleNotification extends JWindow implements Runnable {
	
	private static final Color BORDER_COLOR = new Color(160,200,255);
	private static final Color BACKGROUND_COLOR = new Color(240,250,255);

	private JEditorPane _editorPane = new JEditorPane();

	private int _seconds;
	private int _desktopWidth;
	private int _desktopHeight;
	
	private static final int _width = 150;
	private static final int _height = 55;

	public SimpleNotification(String text, int seconds) {
		discoverDesktopSize();
		setLayout(new BorderLayout());
		_seconds = seconds;
		_editorPane.setEditable(false);
		_editorPane.setContentType("text/html");
		_editorPane.setText(text); //dont worry about trimming. ;-)
		_editorPane.setBackground(BACKGROUND_COLOR);
		_editorPane.setBorder(new CompoundBorder(new EmptyBorder(2,2,2,2),new CompoundBorder(new LineBorder(BORDER_COLOR),new EmptyBorder(2,2,2,2))));
		add(_editorPane, BorderLayout.CENTER);
		setLocation(_desktopWidth-_width,_desktopHeight-_height-28); //28 is the windows bar height (should be dynamic somehow)
		setSize(_width, _height);
		setVisible(false);
	}

	public void run() {
		setVisible(true);
		try { Thread.sleep(_seconds * 1000); } catch (Exception e) {}
		setVisible(false);
	}

	private void discoverDesktopSize() {
		DisplayMode dMode = getGraphicsConfiguration().getDevice().getDisplayMode();
		_desktopWidth = dMode.getWidth();
		_desktopHeight = dMode.getHeight();
	}

	private static final long serialVersionUID = 1L;
}
