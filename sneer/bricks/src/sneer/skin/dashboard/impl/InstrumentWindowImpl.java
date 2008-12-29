package sneer.skin.dashboard.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sneer.skin.dashboard.InstrumentWindow;

class InstrumentWindowImpl extends JPanel implements InstrumentWindow {

	private static final int MINIMAL_TOOLBAR_HEIGHT = 14;

	private static final long serialVersionUID = 1L;
	
	private final JPanel _contentPane = new JPanel();
	
	private final JLayeredPane _toolbarRoot = new JLayeredPane(){
		@Override public Dimension getPreferredSize() { return getToolbarDimension();}
	};
	private final JPanel _toolbarTitleLayer = new JPanel();
	private final GlassPane _toolbarGlassPane = new GlassPane(_contentPane, _toolbarTitleLayer){
		@Override public Dimension getPreferredSize() { return getToolbarDimension();}
	};
	private final JPanel _toolbarFakeMarginLayer = new JPanel(){
		@Override public Dimension getPreferredSize() { return getToolbarDimension();}
	};
	
	private final JPanel _actions = new JPanel();
	private final JLabel _title = new JLabel();
	
	public InstrumentWindowImpl(String title) {
		if(title!=null) _title.setText(title);
		
		_title.setFont(_title.getFont().deriveFont(10f));
		_title.setBorder(new EmptyBorder(0,10,0,0));
		
		setLayout(new BorderLayout());
		add(_contentPane, BorderLayout.CENTER);
		add(_toolbarRoot, BorderLayout.NORTH);
		
		JPanel tmp = new JPanel();
		tmp.setOpaque(false);
		_toolbarFakeMarginLayer.setLayout(new GridBagLayout());
		_toolbarFakeMarginLayer.add(tmp, new GridBagConstraints(0,0, 1,1, 1.0,1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0));
		
		tmp = new JPanel();
		tmp.setBackground(Color.WHITE);
		_toolbarFakeMarginLayer.add(tmp, new GridBagConstraints(0,1, 1,1, 1.0,1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0));
		
		_toolbarRoot.add(_toolbarFakeMarginLayer,  new Integer(0));
		_toolbarRoot.add(_toolbarTitleLayer, new Integer(1));
		_toolbarRoot.add(_toolbarGlassPane, new Integer(2));
		
		_toolbarTitleLayer.setVisible(false);
		_toolbarFakeMarginLayer.setOpaque(false);
		_toolbarRoot.setOpaque(false);
		_actions.setOpaque(false);
		setOpaque(false);
		
		_toolbarTitleLayer.setBackground(Color.WHITE);
		_contentPane.setBackground(Color.WHITE);
		
		_toolbarTitleLayer.setLayout(new BorderLayout());
		_toolbarTitleLayer.add(_title, BorderLayout.WEST);
		_toolbarTitleLayer.add(_actions, BorderLayout.CENTER);
		_actions.setBorder(new EmptyBorder(0,0,0,4));
		
		_actions.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 0));
		initWindowResizeListener();
		
		_toolbarGlassPane.addMouseListener(new MouseAdapter(){
			@Override public void mouseEntered(MouseEvent e) { tryShowToolbar(); }
			@Override public void mouseExited(MouseEvent e) { tryHideToolbar(); }
		});
	}

	private Dimension getToolbarDimension() {
		return new Dimension(getWidth(), getToolbarHeight());
	}

	private int getToolbarHeight() {
		int h = (int)_toolbarTitleLayer.getPreferredSize().getHeight();
		h = (h<MINIMAL_TOOLBAR_HEIGHT)?MINIMAL_TOOLBAR_HEIGHT:h;
		return h;
	}

	void tryShowToolbar() {
		if (_actions.getComponentCount() == 0) return;
		_toolbarTitleLayer.setVisible(true);
	}

	void tryHideToolbar() {
		_toolbarTitleLayer.setVisible(false);
	}

	private void initWindowResizeListener() {
		addHierarchyBoundsListener(new HierarchyBoundsAdapter(){@Override public void ancestorResized(HierarchyEvent e) {
			resizeContents();
		}});
	}
	
	void resizeContents() {
		_toolbarRoot.setBounds(0, 0, this.getWidth(), getToolbarHeight());
		_toolbarTitleLayer.setBounds(0, 0, this.getWidth(), getToolbarHeight());
		_toolbarGlassPane.setBounds(0, 0, this.getWidth(), getToolbarHeight());
		_toolbarFakeMarginLayer.setBounds(0, 0, this.getWidth(), getToolbarHeight());
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
	public Container actions() {
		return _actions;
	}
	
	public static void main(String[] args) {
		final JFrame frm = new JFrame();
		frm.setBounds(10, 10, 500, 300);
		frm.getContentPane().setLayout(new BorderLayout());
		frm.getContentPane().setBackground(Color.GRAY);
		
		InstrumentWindowImpl instrument = new InstrumentWindowImpl("Teste");
		frm.getContentPane().add(instrument, BorderLayout.CENTER);
		instrument.actions().add(new JButton("1"));		
		instrument.actions().add(new JButton("2"));
		instrument.contentPane().add(new JButton("3"));
		
		frm.setVisible(true);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}