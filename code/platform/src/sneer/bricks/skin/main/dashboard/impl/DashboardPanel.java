package sneer.bricks.skin.main.dashboard.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

import sneer.bricks.hardware.gui.Action;
import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.skin.main.dashboard.InstrumentPanel;
import sneer.bricks.skin.main.instrumentregistry.Instrument;
import sneer.bricks.skin.main.synth.Synth;
import sneer.bricks.skin.menu.MenuFactory;
import sneer.bricks.skin.menu.MenuGroup;
import sneer.bricks.software.bricks.introspection.Introspector;

class DashboardPanel extends JPanel {

	private final Synth _synth = my(Synth.class);
	
	private final int INTRUMENTS_GAP = synthValue("Dashboard.INTRUMENTS_GAP");  
	private final int TOOLBAR_HEIGHT = synthValue("Dashboard.TOOLBAR_HEIGHT");  
	private final int SHADOW_HEIGHT = synthValue("Dashboard.SHADOW_HEIGHT");  
	private final int VERTICAL_MARGIN = synthValue("Dashboard.VERTICAL_MARGIN");  
	private final int INSTRUMENT_BORDER = synthValue("Dashboard.INSTRUMENT_BORDER");  
	
	private final JLayeredPane _dashboardLayeredPane = new JLayeredPane();
	private final JPanel _instrumentsContainer = new JPanel();
	private final List<InstrumentPanelImpl> _instrumentPanels = new ArrayList<InstrumentPanelImpl>();
	private final JScrollBar _scrollBar;

	private final InstrumentInstaller _instrumentInstaller = new InstrumentInstaller();

	DashboardPanel(JScrollBar scrollBar) {
		_scrollBar = scrollBar;
		
		initSynth();
		setLayout(new BorderLayout());
    	addInstrumentPanelResizer();

    	_scrollBar.setBlockIncrement(80);
    	_scrollBar.setUnitIncrement(40);
    	_scrollBar.setSize(10, _scrollBar.getSize().height);
    	
		add(_dashboardLayeredPane, BorderLayout.CENTER);
		_scrollBar.getModel().addChangeListener(new ChangeListener(){ @Override public void stateChanged(ChangeEvent event) {
			hideAllToolbars();
			resizeInstruments();
		}});
		
		_dashboardLayeredPane.add(_instrumentsContainer);
        _instrumentsContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, INTRUMENTS_GAP));
    	addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
    		for (InstrumentPanelImpl instrument : _instrumentPanels) 
    			instrument.resizeInstrumentPanel();
		}});
    }

	private int synthValue(String key) {
		return (Integer)_synth.getDefaultProperty(key);
	}

	private void initSynth() {
		_synth.notInGuiThreadAttach(this, "DashboardPanel");
		_synth.notInGuiThreadAttach(_instrumentsContainer, "InstrumentsContainer");
	}
	
	void hideAllToolbars() {
		for (InstrumentPanelImpl panel : _instrumentPanels) 
			panel._toolbar.setVisible(false);
	}
	
	private void addInstrumentPanelResizer() {
		addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
			resizeInstruments();
		}});
	}
	
	private void resizeInstruments() {
		int x = 0;
		int width = getSize().width;
		int height = getSize().height - TOOLBAR_HEIGHT;
		
		int sum = TOOLBAR_HEIGHT;
		for (InstrumentPanelImpl instrument : _instrumentPanels) 
			sum = sum + instrument._instrument.defaultHeight() + INTRUMENTS_GAP;
		int max = sum<height?height:sum;
		
		_scrollBar.getModel().setMinimum(0);
		_scrollBar.getModel().setMaximum(getSize().height);
		
		int hidden = sum-height;
		hidden = (hidden<0)?0:hidden;
		
		_scrollBar.getModel().setExtent(TOOLBAR_HEIGHT+getSize().height-hidden);
		_instrumentsContainer.setBounds(x, TOOLBAR_HEIGHT -offset(), width, max);
	}

	private int offset() {
		return _scrollBar.getModel().getValue();
	}
	
	void install(Instrument instrument) {
		_instrumentInstaller.install(instrument);
	}

	private class InstrumentPanelImpl extends JPanel implements InstrumentPanel{
		
		private boolean _isDocked=true;
		
		private final AbstractLayerUI<JPanel> _instrumentGlasspane;
		private final JXLayer<JPanel> _instrumentJXLayer ;
		private final Toolbar _toolbar;
		private final Instrument _instrument;

		private JPanel _contentPane = new JPanel();
		private JFrame _undockWindow;
		
		InstrumentPanelImpl(Instrument instrument) {
			setLayout(new BorderLayout());
			add(_contentPane, BorderLayout.CENTER);
			
			_synth.notInGuiThreadAttach(this, "InstrumentPanel");
			_instrument = instrument;
			_toolbar = new Toolbar(_instrument.title());
			
			_instrumentGlasspane = new InstrumentGlasspane();
			_instrumentJXLayer = new JXLayer<JPanel>(this, _instrumentGlasspane);
			_instrumentsContainer.add(_instrumentJXLayer);
			_instrumentPanels.add(this);
			_toolbar.setVisible(false); 
			
			initUndockAction();
		}

		private void initUndockAction() {
			_toolbar._menuActions.addAction(new Action(){
				@Override public String caption() { return (_isDocked)?"Undock":"Dock";}
				@Override public void run() { 
					if(_isDocked) undock();
					else dock();
					repaintInstruments();
				}});
		}

		private void repaintInstruments() {
			resizeInstrumentPanel();
			my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
				hideAllToolbars();
				_toolbar.setVisible(true);
			}});
		}
		
		private void dock(){
			_undockWindow.setVisible(false);
			remove(_contentPane);
			add(_contentPane, BorderLayout.CENTER);
			_undockWindow.dispose();			
			_undockWindow = null;
			_isDocked = true;
		}
		
		private void undock(){
			_undockWindow = new JFrame(_instrument.title());
			_undockWindow.setLayout(new BorderLayout());
			
			Rectangle bounds = _contentPane.getBounds();
			Point location = _contentPane.getLocationOnScreen();
			int offset_x = 26;
			int offset_y = 40;
			_undockWindow.setBounds(location.x-offset_x/2, location.y,  bounds.width + offset_x, bounds.height + offset_y);

			remove(_contentPane);
			_undockWindow.add(_contentPane, BorderLayout.CENTER);
			_undockWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			_undockWindow.setVisible(true);
			_isDocked = false;
			_undockWindow.addWindowListener(new WindowAdapter(){ @Override public void windowClosing(WindowEvent e) {
					dock();
					repaintInstruments();
			}});
		}
		
		private void hideAndShow(Point mousePoint) {
			for (InstrumentPanelImpl instrument : _instrumentPanels) 
				instrument._toolbar.setVisible(isMouseOverInstrument(mousePoint, instrument));

			_instrumentsContainer.repaint();
		}
		
		private boolean isMouseOverAnyToolbar(Point mousePoint) {
			for (InstrumentPanelImpl instrument : _instrumentPanels) {
				Toolbar toolbar = instrument._toolbar;
				if(toolbar.isVisible()) {
					JComponent component = instrument._toolbar._toolbarPanel;
					return new Rectangle(component.getLocationOnScreen(), component.getSize()).contains(mousePoint);
				}
			}
			return false;
		}		

		private boolean isMouseOverInstrument(Point mousePoint, InstrumentPanelImpl instrument) {
			return new Rectangle(instrument.getLocationOnScreen(),  instrument.getSize()).contains(mousePoint);
		}

		private class InstrumentGlasspane extends AbstractLayerUI<JPanel> {
			
			@Override
			protected void processMouseEvent(MouseEvent event, JXLayer<? extends JPanel> layer) {
				Point mousePoint = event.getLocationOnScreen();
				if(isMouseOverAnyToolbar(mousePoint)) 
					return;
				
				hideAndShow(mousePoint);
			}
			
			@Override protected void paintLayer(Graphics2D g2, JXLayer<? extends JPanel> layer) {
				super.paintLayer(g2, layer);
				addInstrumentFog(g2, layer);
			}

			private void addInstrumentFog(Graphics2D g2, JXLayer<? extends JPanel> layer) {
				if(_toolbar.isVisible()) return;
				g2.setColor(new Color(1f, 1f, 1f, 0.5f));
				g2.fillRect(0, 0, layer.getWidth(), layer.getHeight());
			}
		}
		
		private class Toolbar{
			private final MenuGroup<JPopupMenu> _menuActions = my(MenuFactory.class).createPopupMenu();
			private final JPanel _toolbarPanel = new JPanel();
			private final JPanel _toolbarShadow = new JPanel(){ @Override public void paint(Graphics g) {
				paintShadows(g);
			}};
			
			private final JButton _mouseBlockButton = new JButton(); //Fix: Remove this hack used to block mouse 
																									//event dispatch to the instrument behind toolbar.
			private final JLabel _title = new JLabel();
			private final JButton _menu = new JButton();
			
			private Toolbar(String title){
				initCopyClassNameToClipboardAction();
				
				initGui(title);
				initSynth();
				
				DashboardPanel.this._dashboardLayeredPane.add(_toolbarShadow, new Integer(1));
				DashboardPanel.this._dashboardLayeredPane.add(_mouseBlockButton, new Integer(2));
				DashboardPanel.this._dashboardLayeredPane.add(_toolbarPanel, new Integer(3));
			}

			private void initCopyClassNameToClipboardAction() {
				_menuActions.addAction(new Action(){
					@Override public String caption() { 
						return "Copy Instrument Name (" + instrumentName() + ")";
					}

					@Override public void run() {
						StringSelection stringSelection = new StringSelection(instrumentInterface().getName());
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(stringSelection, new ClipboardOwner(){ @Override public void lostOwnership(Clipboard clipboard_, Transferable contents) {}});
					}});
			}
			
			private String instrumentName() {
				return instrumentInterface().getSimpleName();
			}

			private Class<?> instrumentInterface() {
				return my(Introspector.class).brickInterfaceFor(_instrument);
			}

			private void initSynth() {
				_synth.notInGuiThreadAttach(_toolbarPanel, "InstrumentToolbar");
				_synth.notInGuiThreadAttach(_title, "InstrumentTitle");
				_synth.notInGuiThreadAttach(_menu, "InstrumentMenuButton");
				_synth.notInGuiThreadAttach(_mouseBlockButton);
			}
			
			private void initGui(String title) {
				_title.setOpaque(false);
				if (title != null)
					_title.setText(title);
				
				_title.setFont(_title.getFont().deriveFont(10f));
				_title.setBorder(new EmptyBorder(0, 10, 0, 0));
				
				_menu.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
					showActionsPopUp();
				}});
				
				_toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
				_toolbarPanel.add(_title);
				_toolbarPanel.add(_menu);
				
				_toolbarShadow.setBorder(new LineBorder(Color.BLACK));
				_toolbarShadow.setOpaque(false);
			}
			
			private void setVisible(boolean isVisible) {
				if(isVisible) resizeToolbar();
				_toolbarPanel.setVisible(isVisible); 	
				_mouseBlockButton.setVisible(isVisible);
				_menu.setVisible(_menuActions.getWidget().getSubElements().length>0);
				_toolbarShadow.setVisible(isVisible);
			}
			
			private void showActionsPopUp() {	_menuActions.getWidget().show(_menu, 0, 15);	}
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
				
				int x = 0-INSTRUMENT_BORDER;
				int y = pointInstrument.y - pointInstrumentContainer.y;
				int width = getSize().width+2*INSTRUMENT_BORDER;
				_toolbarPanel.setBounds(x + VERTICAL_MARGIN, y-offset(), width, TOOLBAR_HEIGHT);
				_mouseBlockButton.setBounds(x, y, width, TOOLBAR_HEIGHT);
				resizeShadow(0, y);
			}

			private void resizeShadow(int x, int y) {
				int width  = _instrumentsContainer.getWidth();
				int height = 2*SHADOW_HEIGHT + TOOLBAR_HEIGHT + getSize().height;
				
				_toolbarShadow.setBounds(x, y-SHADOW_HEIGHT-offset(),  width, height);
			}	
			
			protected void paintShadows(Graphics graph) {
				Graphics2D g2 = (Graphics2D) graph;
				
				Color shadow = new Color(0f, 0f, 0f, 0.3f); //Fix: move to synth
				Color translucent = new Color(1f, 1f, 1f, 0.0f); //Fix: move to synth
				
				g2.setColor(shadow);
				g2.fillRect(0, SHADOW_HEIGHT +TOOLBAR_HEIGHT, VERTICAL_MARGIN-INSTRUMENT_BORDER, (int) getBounds().getMaxY()+INSTRUMENT_BORDER);	
				
				g2.fillRect((int) getBounds().getMaxX()+VERTICAL_MARGIN+INSTRUMENT_BORDER , SHADOW_HEIGHT +TOOLBAR_HEIGHT, 
								VERTICAL_MARGIN-INSTRUMENT_BORDER, (int) getBounds().getMaxY()+INSTRUMENT_BORDER);

				GradientPaint gp;
				gp = new GradientPaint(0, 0 , translucent, 0f,  SHADOW_HEIGHT +TOOLBAR_HEIGHT, shadow);  
				g2.setPaint(gp);  
				g2.fillRect(0, 0, _toolbarShadow.getWidth(), SHADOW_HEIGHT +TOOLBAR_HEIGHT);		
				
				gp = new GradientPaint(0, (int) getBounds().getMaxY() +SHADOW_HEIGHT +TOOLBAR_HEIGHT , shadow, 0,  (int) getBounds().getMaxY() +2*SHADOW_HEIGHT +TOOLBAR_HEIGHT, translucent);  
				g2.setPaint(gp);  
				g2.fillRect(0, (int) getBounds().getMaxY() +SHADOW_HEIGHT +TOOLBAR_HEIGHT+INSTRUMENT_BORDER, _toolbarShadow.getWidth(), SHADOW_HEIGHT);	
				
				Color color1 = new Color(70, 150, 180); //Fix: move to synth
				Color color2 = Color.WHITE; //Fix: move to synth
				
				gp = new GradientPaint(0, SHADOW_HEIGHT-TOOLBAR_HEIGHT , color1, 0,  SHADOW_HEIGHT +TOOLBAR_HEIGHT, color2);  
				g2.setPaint(gp);  
				g2.fillRect(VERTICAL_MARGIN-INSTRUMENT_BORDER, SHADOW_HEIGHT, _toolbarShadow.getWidth()- 2*(VERTICAL_MARGIN-INSTRUMENT_BORDER), TOOLBAR_HEIGHT);		
			}
		}
		
		private void resizeInstrumentPanel() {
			_toolbar.resizeToolbar();
			int width = _instrumentsContainer.getWidth() - VERTICAL_MARGIN*2;
			setSize(new Dimension(width,  (_isDocked)?_instrument.defaultHeight():10));
		}

		@Override public void setSize(Dimension size) {
			setMinimumSize(size);
			setPreferredSize(size);
			super.setSize(size);
		}
		
		@Override public MenuGroup<JPopupMenu> actions() { return _toolbar._menuActions; }
		@Override public Container contentPane() {	return _contentPane ; }
	}
	
	private class InstrumentInstaller{
		private InstrumentPanel install(final Instrument instrument) {
			
			final InstrumentPanelImpl instrumentPanel = new InstrumentPanelImpl(instrument);
			
			my(GuiThread.class).invokeAndWait(new Runnable(){	
				@Override 
				public void run() {
					instrument.init(instrumentPanel);
					instrumentPanel.resizeInstrumentPanel();
					instrumentPanel.revalidate();
				}
			});
//			RunMe.logTree(instrumentPanel);
			return instrumentPanel;
		}
	}
}