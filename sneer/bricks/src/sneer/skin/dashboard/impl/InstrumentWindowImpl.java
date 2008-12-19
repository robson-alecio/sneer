package sneer.skin.dashboard.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import sneer.skin.dashboard.InstrumentWindow;

class InstrumentWindowImpl extends JPanel implements InstrumentWindow {

	private static final long serialVersionUID = 1L;
	
	private final JLayeredPane _root = new JLayeredPane();
	private final JPanel _contentPane = new JPanel();
	private final JPanel _toolbar = new JPanel();
	private final GlassPane _glassPane = new GlassPane(_contentPane, _toolbar);
	
	public InstrumentWindowImpl() {
		setLayout(new BorderLayout());
		add(_root, BorderLayout.CENTER);
		_root.add(_contentPane, new Integer(0));
		_root.add(_toolbar, new Integer(1));
		_root.add(_glassPane, new Integer(2));
		
		_contentPane.setBackground(Color.BLUE);
		
		_toolbar.setOpaque(false);
		_toolbar.setVisible(false);
		
		_toolbar.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 0));
		initWindowResizeListener();
		
		_glassPane.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseEntered(MouseEvent e) {
				tryShowToolbar();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				tryHideToolbar();
			}
			});
	}

	void tryShowToolbar() {
		if (_toolbar.getComponentCount() == 0)
			return;
		_toolbar.setVisible(true);
	}

	void tryHideToolbar() {
		_toolbar.setVisible(false);
	}

	private void initWindowResizeListener() {
		addHierarchyBoundsListener(new HierarchyBoundsAdapter(){@Override public void ancestorResized(HierarchyEvent e) {
			resizeContents();
		}});
	}
	
	void resizeContents() {
		_contentPane.setBounds(0, 0, getWidth(), getHeight());
		_glassPane.setBounds(_contentPane.getBounds());
		Dimension size = _toolbar.getPreferredSize();
		_toolbar.setBounds(getWidth() - size.width, 0, size.width, size.height);
	}
	
	@Override
	public Dimension getPreferredSize() {
		int width = getParent().getWidth()-10;
		if(width<0) width = 0;
		
		int height = (int) super.getPreferredSize().getHeight();
		if(height<0) height = 30;
		
		Dimension dim = new Dimension(width,height);
		return dim;
	}

	@Override
	public Dimension getSize() {
		int width = getParent().getWidth()-10;
		if(width<0) width = 0;
		
		int height = (int) super.getSize().getHeight();
		
		Dimension dim = new Dimension(width,height);
		return dim;		
	}
	
	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();
		bounds.setLocation(10, (int) bounds.getY());
		return bounds;
	}
	
	@Override
	public Container contentPane() {
		return _contentPane;
	}

	@Override
	public Container toolbar() {
		return _toolbar;
	}
	
	public static void main(String[] args) {
		final JFrame frm = new JFrame();
		frm.setBounds(10, 10, 500, 300);
		frm.getContentPane().setLayout(new BorderLayout());
		
		InstrumentWindowImpl instrument = new InstrumentWindowImpl();
		frm.getContentPane().add(instrument, BorderLayout.CENTER);
		instrument.toolbar().add(new JButton("1"));		
		instrument.toolbar().add(new JButton("2"));
		instrument.contentPane().add(new JButton("3"));
		
		frm.setVisible(true);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}