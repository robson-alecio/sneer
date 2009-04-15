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

// 	JFrame
//			RootPane
//				Glasspane
//				DashboardPane (ContentPane)
//					_instrumentsAndToolbarsLayeredPane (JLayeredPane)
//						_toolbarPanel (0..1)
//						_mouseBlockButton (hack to block mouse events)
//						_instrumentsPanel (0..n)
//							_instrumentLayer (decorator)
//								_instrumentGlasspane (mouse listener)
//								InstrumentWindow (instrument container)
public class DashboardPane extends JPanel {

	private static final Image ACTIONS = my(Images.class).getImage(DashboardPane.class.getResource("menu.png"));
	private final JLayeredPane _instrumentsAndToolbarsLayeredPane = new JLayeredPane();
	private final JPanel _instrumentsPanel = new JPanel();
	private final InstrumentInstaller _installer = new InstrumentInstaller();
	private List<InstrumentWindowImpl> _instruments = new ArrayList<InstrumentWindowImpl>();
	
	public DashboardPane()    {
//    	setBackground(Color.RED);
//     _instrumentsPanel.setBackground(Color.YELLOW);

		setLayout(new BorderLayout());
    	addInstrumentPanelResizer();

       	add(_instrumentsAndToolbarsLayeredPane, BorderLayout.CENTER);
        _instrumentsAndToolbarsLayeredPane.add(_instrumentsPanel);
        _instrumentsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 1));
    	addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
    		for (InstrumentWindowImpl instrument : _instruments) {
    			instrument.resizeInstrumentWindow();
    		}
		}});        
    }
	
	private void addInstrumentPanelResizer() {
		addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
			int x = 0;
			int y = Toolbar._TOOLBAR_HEIGHT;
			int width = getSize().width;
			int height = getSize().height - Toolbar._TOOLBAR_HEIGHT;
			_instrumentsPanel.setBounds(x, y, width, height);
		}});
	}
	
	public void install(Instrument instrument) {
		_installer.install(instrument);
	}
	
	class InstrumentWindowImpl extends JPanel implements InstrumentWindow{
		private final AbstractLayerUI<JPanel> _instrumentGlasspane;
		private final JXLayer<JPanel> _instrumentLayer ;
		private final Toolbar _toolbar;
		private final Instrument _instrument;
		
		InstrumentWindowImpl(Instrument instrument) {
			_instrument = instrument;
			_toolbar = new Toolbar(this);
			_instrumentGlasspane = new InstrumentGlasspane(_toolbar);
			_instrumentLayer = new JXLayer<JPanel>(this, _instrumentGlasspane);
			_instrumentsPanel.add(_instrumentLayer);
			_instruments.add(this);

			_toolbar.setVisible(false); 
//			_toolbar.setVisible(true); 
//			_toolbar._toolbarPanel.setBackground(Color.RED);
		}

		@Override
		protected void finalize() throws Throwable {
			_instruments.remove(this);
			super.finalize();
		}
		
		private void resizeInstrumentWindow() {
			Dimension size = new Dimension(_instrumentsPanel.getWidth(), _instrument.defaultHeight());
			setMinimumSize(size);
			setPreferredSize(size);
			setSize(size);
			_toolbar.resizeToolbar();
		}

		@Override public JPopupMenu actions() { return _toolbar._menuActions; }
		@Override public Container contentPane() {	return this; }
	}
	
	class Toolbar{
		private static final int _TOOLBAR_HEIGHT = 20;
		
		private final JPopupMenu _menuActions = new JPopupMenu();
		private final JPanel _toolbarPanel = new JPanel();
		private final JButton _mouseBlockButton = new JButton(); //Fix: Remove this hack used to block mouse 
																								//event dispatch to the instrument behind toolbar 
		private final InstrumentWindowImpl _instrumentWindowImpl;
		private final JLabel _title = new JLabel();
		private final JLabel _menu = new JLabel(new ImageIcon(ACTIONS)){
			@Override public boolean isVisible() {
				return _menuActions.getSubElements().length>0;
			}
		};
		
		private Toolbar(InstrumentWindowImpl instrumentWindowImpl){
			_instrumentWindowImpl = instrumentWindowImpl;
			initGui(instrumentWindowImpl._instrument.title());
			DashboardPane.this._instrumentsAndToolbarsLayeredPane.add(_mouseBlockButton, new Integer(1));
			DashboardPane.this._instrumentsAndToolbarsLayeredPane.add(_toolbarPanel, new Integer(2));
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
			Point pointInstrument = _instrumentWindowImpl.getLocation();
			
			int x = 0;
			int y = pointInstrument.y;
			int width = getSize().width;
			int height = Toolbar._TOOLBAR_HEIGHT;
			_toolbarPanel.setBounds(x, y, width, height);
			_mouseBlockButton.setBounds(x, y, width, height);
		}	
		
		private boolean isOverAnyToolbar(Point mousePoint) {
			for (InstrumentWindowImpl instrument : _instruments) {
				Toolbar toolbar = instrument._toolbar;
				if(toolbar.isVisible())
					return getAreaOnScreen(instrument._toolbar._toolbarPanel).contains(mousePoint);
			}
			return false;
		}

		private void hideAndShow(Point mousePoint) {
			for (InstrumentWindowImpl instrument : _instruments) 
				instrument._toolbar.setVisible(getAreaOnScreen(instrument).contains(mousePoint));
		}

		private Rectangle getAreaOnScreen(JComponent component) {
			return new Rectangle(component.getLocationOnScreen(), component.getSize());
		}
	}	
	
	class InstrumentGlasspane extends AbstractLayerUI<JPanel> {
		private final Toolbar _toolbar;
		
		public InstrumentGlasspane(Toolbar toolbar) { _toolbar = toolbar; }
		
		@Override protected void processMouseMotionEvent(MouseEvent event, JXLayer<JPanel> layer) {
			System.out.println(event.getPoint());
			Point mousePoint = event.getLocationOnScreen();
			if(_toolbar.isOverAnyToolbar(mousePoint)) 
				return;
			
			_toolbar.hideAndShow(mousePoint);
		}

		@Override protected void paintLayer(Graphics2D g2, JXLayer<JPanel> l) {
			super.paintLayer(g2, l);
			g2.setColor(new Color(0, 100, 0, 100));
			g2.fillRect(0, 0, l.getWidth(), l.getHeight());
		}
		
	};
	
	class InstrumentInstaller{
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

			_instrumentsPanel.add(instrumentWindow);
			return instrumentWindow;
		}
	}
}