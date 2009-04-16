package sneer.skin.main.dashboard.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.IllegalComponentStateException;
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
import sneer.skin.main.dashboard.InstrumentPanel;
import sneer.skin.main.instrumentregistry.Instrument;

//	JFrame
//		RootPane
//			LayeredPane
//				Glasspane
//				ContentPane <-- DashboardPanel (JFrame.ContentPane)
// --------------------------------------------------------------------------------------------
//					_dashboardLayeredPane (0..n toolbars, 0..n block button, 1 instrumentsContainer )
//						_toolbarPanel (toolbar container)
//						_mouseBlockButton (hack to block mouse events)
//						_instrumentsContainer (0..n instruments)
//							_instrumentJXLayer (0..n _instrumentJXLayer)
//								_instrumentGlasspane (mouse listener)
//								InstrumentPanel (instrument container)
//
// 	JFrame.JRootPane.JLayeredPane.DashboardPane.JLayeredPane.JPanel.JXLayer.InstrumentWindowImpl
class DashboardPanel extends JPanel {

	private static final int _TOOLBAR_HEIGHT = 20;
	private static final Image TOOLBAR_MENU_IMAGE = my(Images.class).getImage(DashboardPanel.class.getResource("menu.png"));
	
	private final JLayeredPane _dashboardLayeredPane = new JLayeredPane();
	private final JPanel _instrumentsContainer = new JPanel();
	private final List<InstrumentPanelImpl> _instrumentPanels = new ArrayList<InstrumentPanelImpl>();

	private final InstrumentInstaller _instrumentInstaller = new InstrumentInstaller();
	
	DashboardPanel()    {
		setLayout(new BorderLayout());
    	addInstrumentPanelResizer();
    	setBackground(Color.BLACK); //Fix
    	_instrumentsContainer.setBackground(Color.BLUE); //Fix
    	
       	add(_dashboardLayeredPane, BorderLayout.CENTER);
        _dashboardLayeredPane.add(_instrumentsContainer);
        _instrumentsContainer.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 1));
    	addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
    		for (InstrumentPanelImpl instrument : _instrumentPanels) 
    			instrument.resizeInstrumentWindow();
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
	
	private class InstrumentPanelImpl extends JPanel implements InstrumentPanel{
		
		private final AbstractLayerUI<JPanel> _instrumentGlasspane;
		private final JXLayer<JPanel> _instrumentJXLayer ;
		private final Toolbar _toolbar;
		private final Instrument _instrument;
		
		InstrumentPanelImpl(Instrument instrument) {
			_instrument = instrument;
			_toolbar = new Toolbar(_instrument.title());
			_instrumentGlasspane = new InstrumentGlasspane();
			_instrumentJXLayer = new JXLayer<JPanel>(this, _instrumentGlasspane);
			_instrumentsContainer.add(_instrumentJXLayer);
			_instrumentPanels.add(this);

			_toolbar.setVisible(false); 
			_toolbar._toolbarPanel.setBackground(Color.RED); //Fix
		}
		
		private boolean isOverAnyToolbar(Point mousePoint) {
			for (InstrumentPanelImpl instrument : _instrumentPanels) {
				Toolbar toolbar = instrument._toolbar;
				if(toolbar.isVisible()) {
					JComponent component = instrument._toolbar._toolbarPanel;
					return new Rectangle(component.getLocationOnScreen(), component.getSize()).contains(mousePoint);
				}
			}
			return false;
		}		
		
		private void hideAndShow(Point mousePoint) {
			for (InstrumentPanelImpl instrument : _instrumentPanels) 
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
			
//			@Override protected void paintLayer(Graphics2D g2, JXLayer<JPanel> l) {
//				super.paintLayer(g2, l);
//				g2.setColor(new Color(0, 100, 0, 100));
//				g2.fillRect(0, 0, l.getWidth(), l.getHeight());
//			}
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
				DashboardPanel.this._dashboardLayeredPane.add(_mouseBlockButton, new Integer(1));
				DashboardPanel.this._dashboardLayeredPane.add(_toolbarPanel, new Integer(2));
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
				Point pointInstrument;
				Point pointInstrumentContainer;
				try {
					pointInstrument = getLocationOnScreen();
					pointInstrumentContainer = _instrumentsContainer.getLocationOnScreen();
				} catch (IllegalComponentStateException e) {
					return;
				}
				
				int x = 0;
				int y = pointInstrument.y - pointInstrumentContainer.y;
				int width = getSize().width;
				_toolbarPanel.setBounds(x, y, width, _TOOLBAR_HEIGHT);
				_mouseBlockButton.setBounds(x, y, width, _TOOLBAR_HEIGHT);
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
		private InstrumentPanel install(final Instrument instrument) {
			
			final InstrumentPanelImpl instrumentWindow = new InstrumentPanelImpl(instrument);
			
			my(GuiThread.class).strictInvokeAndWait(new Runnable(){	
				@Override 
				public void run() {
					instrument.init(instrumentWindow);
					instrumentWindow.resizeInstrumentWindow();
					instrumentWindow.revalidate();
				}
			});

			return instrumentWindow;
		}
	}
}