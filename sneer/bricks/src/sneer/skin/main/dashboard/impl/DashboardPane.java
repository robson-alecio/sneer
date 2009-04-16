package sneer.skin.main.dashboard.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

import sneer.hardware.gui.guithread.GuiThread;
import sneer.hardware.gui.images.Images;
import sneer.skin.main.dashboard.InstrumentWindow;
import sneer.skin.main.instrumentregistry.Instrument;

//	JFrame
//		RootPane
//			Glasspane
//			ContentPane
// --------------------------------------------------------------------------------------------
//				DashboardPane (JFrame.ContentPane)
//					_rootLayeredPane (DashboardPane)
//						_toolbarPanel (Toolbar)
//						_mouseBlockButton (Toolbar - hack to block mouse events)
//						_instrumentsContainer (DashboardPane)
//							_instrumentJXLayer (InstrumentWindowImpl - decorator)
//								_instrumentGlasspane (InstrumentWindowImpl - mouse listener)
//								InstrumentWindow (instrument container)
//
class DashboardPane extends JPanel {

	private static final int _TOOLBAR_HEIGHT = 20;
	private static final Image TOOLBAR_MENU_IMAGE = my(Images.class).getImage(DashboardPane.class.getResource("menu.png"));
	
	private final JLayeredPane _rootLayeredPane = new JLayeredPane();
	private final JPanel _instrumentsContainer = new JPanel();
	private final List<InstrumentWindowImpl> _instrumentWindows = new ArrayList<InstrumentWindowImpl>();

	private final InstrumentInstaller _instrumentInstaller = new InstrumentInstaller();
	
	DashboardPane()    {
		setLayout(new BorderLayout());
    	addInstrumentPanelResizer();

       	add(_rootLayeredPane, BorderLayout.CENTER);
        _rootLayeredPane.add(_instrumentsContainer);
        _instrumentsContainer.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 1));
    	addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
    		for (InstrumentWindowImpl instrument : _instrumentWindows) {
    			instrument.resizeInstrumentWindow();
    		}
		}});        
    }
	
	private void addInstrumentPanelResizer() {
		addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
			int x = 0;
			int y = _TOOLBAR_HEIGHT;
			int width = getSize().width;
			int height = getSize().height - _TOOLBAR_HEIGHT;
			_instrumentsContainer.setBounds(x, y, width, height);
		}});
	}
	
	void install(Instrument instrument) {
		_instrumentInstaller.install(instrument);
	}
	
	private class InstrumentWindowImpl extends JPanel implements InstrumentWindow{
		
		private final AbstractLayerUI<JPanel> _instrumentGlasspane;
		private final JXLayer<JPanel> _instrumentJXLayer ;
		private final Toolbar _toolbar;
		private final Instrument _instrument;
		
		InstrumentWindowImpl(Instrument instrument) {
			_instrument = instrument;
			_toolbar = new Toolbar(_instrument.title());
			_instrumentGlasspane = new InstrumentGlasspane();
			_instrumentJXLayer = new JXLayer<JPanel>(this, _instrumentGlasspane);
			_instrumentsContainer.add(_instrumentJXLayer);
			_instrumentWindows.add(this);

//			_toolbar.setVisible(false); 
			_toolbar.setVisible(true); 
			_toolbar._toolbarPanel.setBackground(Color.RED);
		}
		
		private boolean isOverAnyToolbar(Point mousePoint) {
			for (InstrumentWindowImpl instrument : _instrumentWindows) {
				Toolbar toolbar = instrument._toolbar;
				if(toolbar.isVisible()) {
					JComponent component = instrument._toolbar._toolbarPanel;
					return new Rectangle(component.getLocationOnScreen(), component.getSize()).contains(mousePoint);
				}
			}
			return false;
		}		
		
		private void hideAndShow(Point mousePoint) {
			for (InstrumentWindowImpl instrument : _instrumentWindows) 
				instrument._toolbar.setVisible(new Rectangle(instrument.getLocationOnScreen(), 
																				  instrument.getSize()).contains(mousePoint));
		}

		private class InstrumentGlasspane extends AbstractLayerUI<JPanel> {
			
			@Override protected void processMouseMotionEvent(MouseEvent event, JXLayer<JPanel> layer) {
				Point mousePoint = event.getLocationOnScreen();
				if(isOverAnyToolbar(mousePoint)) 
					return;
				
				hideAndShow(mousePoint);
			}
			
			@Override protected void paintLayer(Graphics2D g2, JXLayer<JPanel> l) {
				super.paintLayer(g2, l);
				g2.setColor(new Color(0, 100, 0, 100));
				g2.fillRect(0, 0, l.getWidth(), l.getHeight());
			}
		}
		
		private class Toolbar{
			
			private final JPopupMenu _menuActions = new JPopupMenu();
			private final JPanel _toolbarPanel = new JPanel();
			private final JButton _mouseBlockButton = new JButton(); //Fix: Remove this hack used to block mouse 
																									//event dispatch to the instrument behind toolbar 
			private final JLabel _title = new JLabel();
			private final JLabel _menu = new JLabel(new ImageIcon(TOOLBAR_MENU_IMAGE)){
				@Override public boolean isVisible() {
					return _menuActions.getSubElements().length>0;
			}};
			
			private Toolbar(String title){
				initGui(title);
				DashboardPane.this._rootLayeredPane.add(_mouseBlockButton, new Integer(1));
				DashboardPane.this._rootLayeredPane.add(_toolbarPanel, new Integer(2));
			}
			
			private void initGui(String title) {
				_title.setOpaque(false);
				if (title != null)
					_title.setText(title);
				
				_title.setFont(_title.getFont().deriveFont(10f));
				_title.setBorder(new EmptyBorder(0, 10, 0, 0));
				
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
				
				_toolbarPanel.setLayout(new BorderLayout());
				_toolbarPanel.add(_title, BorderLayout.CENTER);
				_toolbarPanel.add(_menu, BorderLayout.EAST);
			}
			
			private void setVisible(boolean isVisible) {
				if(isVisible) resizeToolbar();
				_toolbarPanel.setVisible(isVisible); 	
				_mouseBlockButton.setVisible(isVisible);
			}
			
			private void showActionsPopUp() {	_menuActions.show(_menu, 0, 15);	}
			private boolean isVisible() { 	return _toolbarPanel.isVisible(); 	}
			
			private void resizeToolbar() {
				Point pointInstrument = getLocation();
				
				int x = 0;
				int y = pointInstrument.y;
				int width = getSize().width;
				int height = _TOOLBAR_HEIGHT;
				_toolbarPanel.setBounds(x, y, width, height);
				_mouseBlockButton.setBounds(x, y, width, height);
			}	
		}
		
		private void resizeInstrumentWindow() {
			Dimension size = new Dimension(_instrumentsContainer.getWidth(), _instrument.defaultHeight());
			setMinimumSize(size);
			setPreferredSize(size);
			setSize(size);
			_toolbar.resizeToolbar();
		}

		@Override public JPopupMenu actions() { return _toolbar._menuActions; }
		@Override public Container contentPane() {	return this; }
	}
	
	private class InstrumentInstaller{
		private InstrumentWindow install(final Instrument instrument) {
			
			final InstrumentWindowImpl instrumentWindow = new InstrumentWindowImpl(instrument);
			
			my(GuiThread.class).strictInvokeAndWait(new Runnable(){	
				@Override 
				public void run() {
					instrument.init(instrumentWindow);
					instrumentWindow.resizeInstrumentWindow();
					instrumentWindow.revalidate();
				}
			});

			_instrumentsContainer.add(instrumentWindow);
			return instrumentWindow;
		}
	}
}