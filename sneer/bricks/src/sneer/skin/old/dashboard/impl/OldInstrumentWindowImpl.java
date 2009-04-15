package sneer.skin.old.dashboard.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import sneer.hardware.gui.images.Images;
import sneer.skin.colors.Colors;
import sneer.skin.old.dashboard.OldInstrumentWindow;

class OldInstrumentWindowImpl extends JPanel implements OldInstrumentWindow {
	
	private static final Image ACTIONS = getImage("menu.png");
	private static final int MINIMAL_TOOLBAR_HEIGHT = 14;
	private static final long serialVersionUID = 1L;
	
	private final JPanel _contentPane = new JPanel();
	
	private final JLayeredPane _toolbarRoot = new JLayeredPane(){
		@Override public Dimension getPreferredSize() { return getToolbarDimension();}
	};
	private final JPanel _toolbarTitleLayer =  new JPanel();//new GradientPanel();
	
	private final JPanel _actions =  new JPanel();
	private final JPopupMenu _menuActions = new JPopupMenu();
	private final JLabel _menu = new JLabel(new ImageIcon(ACTIONS)){
		@Override public boolean isVisible() {
			return _menuActions.getSubElements().length>0;
		}
	};
	private final JLabel _title = new JLabel();
	
//	private class GradientPanel extends JPanel{
//		@Override protected void paintComponent( Graphics g ) {
//
//			int w = getWidth( );
//			int h = getHeight( );
//			
//			Graphics2D g2d = (Graphics2D)g;
//			GradientPaint gp = new GradientPaint(0, 0, my(Colors.class).lowContrast(),  0, h, my(Colors.class).solid());
//
//			g2d.setPaint( gp );
//			g2d.fillRect( 0, 0, w, h );		
//			
//		    setOpaque( false );
//		    super.paintComponent( g );
//		    setOpaque( true );
//		}	
//	}
	
	private static Image getImage(String fileName) {
		return my(Images.class).getImage(OldInstrumentWindowImpl.class.getResource(fileName));
	}
	
	public OldInstrumentWindowImpl(String title) {
		_title.setOpaque(false);
		if(title!=null) _title.setText(title);
		
		_title.setFont(_title.getFont().deriveFont(10f));
		_title.setBorder(new EmptyBorder(0,10,0,0));
		
		setLayout(new BorderLayout());
		add(_contentPane, BorderLayout.CENTER);
		add(_toolbarRoot, BorderLayout.NORTH);
		
		_toolbarRoot.add(_toolbarTitleLayer, new Integer(0));
		_toolbarRoot.setOpaque(false);
		setOpaque(false);
		
//		_toolbarTitleLayer.setBackground(my(Colors.class).solid());
		_contentPane.setBackground(my(Colors.class).solid());
		
		_toolbarTitleLayer.setLayout(new BorderLayout());
		_toolbarTitleLayer.add(_title, BorderLayout.WEST);
		_toolbarTitleLayer.add(_actions, BorderLayout.CENTER);
		_actions.setBorder(new EmptyBorder(0,0,0,0));
		
		_actions.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		_actions.add(_menu);
		
		_menu.setBorder(new EmptyBorder(0,0,0,2));
		_menu.setOpaque(false);
		_menu.addMouseListener(new MouseAdapter(){ @Override public void mouseReleased(MouseEvent e) { 
			showActionsPopUp();
		}});
		
		_menu.addMouseListener(new MouseAdapter(){
			{_menu.setEnabled(false);}
			@Override public void mouseEntered(MouseEvent e) {	_menu.setEnabled(true); }
			@Override public void mouseExited(MouseEvent e) {	_menu.setEnabled(false); }
		});
		
		initWindowResizeListener();
	}
	
	private void showActionsPopUp() {
		_menuActions.show(_menu, 0, 15);
	}

	private Dimension getToolbarDimension() {
		return new Dimension(getWidth(), getToolbarHeight());
	}

	private int getToolbarHeight() {
		int h = (int)_toolbarTitleLayer.getPreferredSize().getHeight();
		h = (h<MINIMAL_TOOLBAR_HEIGHT)?MINIMAL_TOOLBAR_HEIGHT:h;
		return h;
	}

	private void initWindowResizeListener() {
		addHierarchyBoundsListener(new HierarchyBoundsAdapter(){@Override public void ancestorResized(HierarchyEvent e) {
			resizeContents();
		}});
	}
	
	void resizeContents() {
		_toolbarRoot.setBounds(0, 0, this.getWidth(), getToolbarHeight());
		_toolbarTitleLayer.setBounds(0, 0, this.getWidth(), getToolbarHeight());
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
	public JPopupMenu actions() {
		return _menuActions;
	}
}