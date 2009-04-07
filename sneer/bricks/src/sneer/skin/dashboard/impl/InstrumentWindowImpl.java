package sneer.skin.dashboard.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sneer.skin.colors.Colors;
import sneer.skin.dashboard.InstrumentWindow;

class InstrumentWindowImpl extends JPanel implements InstrumentWindow {

	private static final int MINIMAL_TOOLBAR_HEIGHT = 14;

	private static final long serialVersionUID = 1L;
	
	private final JPanel _contentPane = new JPanel();
	
	private final JLayeredPane _toolbarRoot = new JLayeredPane(){
		@Override public Dimension getPreferredSize() { return getToolbarDimension();}
	};
	private final JPanel _toolbarTitleLayer =  new GradientPanel();
	private final GlassPane _toolbarGlassPane = new GlassPane(_contentPane, _toolbarTitleLayer){
		@Override public Dimension getPreferredSize() { return getToolbarDimension();}
	};
	private final JPanel _toolbarFakeMarginLayer = new JPanel(){
		@Override public Dimension getPreferredSize() { return getToolbarDimension();}
	};
	
	private final JPanel _actions = new GradientPanel();
	
	private class GradientPanel extends JPanel{
		@Override protected void paintComponent( Graphics g ) {

			int w = getWidth( );
			int h = getHeight( );
			
			Graphics2D g2d = (Graphics2D)g;
			GradientPaint gp = new GradientPaint(0, 0, my(Colors.class).moderateContrast(),  0, h/2, my(Colors.class).solid());

			g2d.setPaint( gp );
			g2d.fillRect( 0, 0, w, h/2 );		
			g2d.setBackground(my(Colors.class).solid());
			g2d.fillRect( 0, h/2, w, h );
			
		    setOpaque( false );
		    super.paintComponent( g );
		    setOpaque( true );
		}	
	}
	
	private final JLabel _title = new JLabel();
	
	public InstrumentWindowImpl(String title) {
		_title.setOpaque(false);
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
		tmp.setBackground(my(Colors.class).solid());
		_toolbarFakeMarginLayer.add(tmp, new GridBagConstraints(0,1, 1,1, 1.0,1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0));
		
		_toolbarRoot.add(_toolbarFakeMarginLayer,  new Integer(0));
		_toolbarRoot.add(_toolbarTitleLayer, new Integer(1));
		_toolbarRoot.add(_toolbarGlassPane, new Integer(2));
		
		_toolbarTitleLayer.setVisible(false);
		
		_toolbarFakeMarginLayer.setOpaque(false);
		_toolbarRoot.setOpaque(false);
//		_actions.setOpaque(false);
		setOpaque(false);
		
		_toolbarTitleLayer.setBackground(my(Colors.class).solid());
		_contentPane.setBackground(my(Colors.class).solid());
		
		_toolbarTitleLayer.setLayout(new BorderLayout());
		_toolbarTitleLayer.add(_title, BorderLayout.WEST);
		_toolbarTitleLayer.add(_actions, BorderLayout.CENTER);
		_actions.setBorder(new EmptyBorder(0,0,0,0));
		
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
}