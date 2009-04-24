package sneer.skin.main.dashboard.impl;

import static sneer.commons.environments.Environments.my;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

import sneer.hardware.gui.guithread.GuiThread;
import sneer.skin.main.dashboard.InstrumentPanel;
import sneer.skin.main.instrumentregistry.Instrument;
import sneer.skin.main.synth.Synth;

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

	private final InstrumentInstaller _instrumentInstaller = new InstrumentInstaller();
	
	DashboardPanel() {
		initSynth();
		setLayout(new BorderLayout());
    	addInstrumentPanelResizer();

    	add(_dashboardLayeredPane, BorderLayout.CENTER);
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
		setName("DashboardPanel");
		_instrumentsContainer.setName("InstrumentsContainer");
		_synth.attach(this);
		_synth.attach(_instrumentsContainer);
	}
	
	void hideAllToolbars() {
		for (InstrumentPanelImpl panel : _instrumentPanels) 
			panel._toolbar.setVisible(false);
	}
	
	private void addInstrumentPanelResizer() {
		addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
			int x = 0;
			int y = TOOLBAR_HEIGHT;
			int width = getSize().width;
			int height = getSize().height - TOOLBAR_HEIGHT;
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
			initSynth();
			_instrument = instrument;
			_toolbar = new Toolbar(_instrument.title());
			_instrumentGlasspane = new InstrumentGlasspane();
			_instrumentJXLayer = new JXLayer<JPanel>(this, _instrumentGlasspane);
			_instrumentsContainer.add(_instrumentJXLayer);
			_instrumentPanels.add(this);
			_toolbar.setVisible(false); 
		}

		private void initSynth() {
			setName("InstrumentPanel");
			_synth.attach(this);
		}
		
		private void hideAndShow(Point mousePoint) {
			for (InstrumentPanelImpl instrument : _instrumentPanels) {
				instrument._toolbar.setVisible(isMouseOverInstrument(mousePoint, instrument));
			}
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
			
			@Override protected void processMouseMotionEvent(MouseEvent event, JXLayer<JPanel> layer) {
				Point mousePoint = event.getLocationOnScreen();
				if(isMouseOverAnyToolbar(mousePoint)) 
					return;
				
				hideAndShow(mousePoint);
			}
			
			@Override protected void paintLayer(Graphics2D g2, JXLayer<JPanel> layer) {
				super.paintLayer(g2, layer);
				addInstrumentFog(g2, layer);
			}

			private void addInstrumentFog(Graphics2D g2, JXLayer<JPanel> layer) {
				if(_toolbar.isVisible()) return;
				g2.setColor(new Color(1f, 1f, 1f, 0.5f));
				g2.fillRect(0, 0, layer.getWidth(), layer.getHeight());
			}
		}
		
		private class Toolbar{
			private final JPopupMenu _menuActions = new JPopupMenu();
			private final JPanel _toolbarPanel = new JPanel();
			private final JPanel _toolbarShadow = new JPanel(){ @Override public void paint(Graphics g) {
				paintShadows(g);
			}};	
			
			private final JButton _mouseBlockButton = new JButton(); //Fix: Remove this hack used to block mouse 
																									//event dispatch to the instrument behind toolbar 
			private final JLabel _title = new JLabel();
			private final JButton _menu = new JButton();
			
			private Toolbar(String title){
				initGui(title);
				initSynth();
				DashboardPanel.this._dashboardLayeredPane.add(_toolbarShadow, new Integer(1));
				DashboardPanel.this._dashboardLayeredPane.add(_mouseBlockButton, new Integer(2));
				DashboardPanel.this._dashboardLayeredPane.add(_toolbarPanel, new Integer(3));
			}

			private void initSynth() {
				_toolbarPanel.setName("InstrumentToolbar");
				_title.setName("InstrumentTitle");
				_menu.setName("InstrumentMenuButton");
				_menu.setName("InstrumentMenuBlockButton");
				_synth.attach(_toolbarPanel);
				_synth.attach(_title);
				_synth.attach(_menu);
				_synth.attach(_mouseBlockButton);
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
				_menu.setVisible(_menuActions.getSubElements().length>0);
				_toolbarShadow.setVisible(isVisible);
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
				
				int x = 0-INSTRUMENT_BORDER;
				int y = pointInstrument.y - pointInstrumentContainer.y;
				int width = getSize().width+2*INSTRUMENT_BORDER;
				_toolbarPanel.setBounds(x + VERTICAL_MARGIN, y, width, TOOLBAR_HEIGHT);
				_mouseBlockButton.setBounds(x, y, width, TOOLBAR_HEIGHT);
				resizeShadow(0, y);
			}

			private void resizeShadow(int x, int y) {
				int width  = _instrumentsContainer.getWidth();
				int height = 2*SHADOW_HEIGHT + TOOLBAR_HEIGHT + getSize().height;
				
				_toolbarShadow.setBounds(x, y-SHADOW_HEIGHT,  width, height);
			}	
			
			protected void paintShadows(Graphics graph) {
				Graphics2D g2 = (Graphics2D) graph;
				
				int x = 0;
				int y = 0;
				int width = _toolbarShadow.getWidth();
				int height = SHADOW_HEIGHT +TOOLBAR_HEIGHT;
				
				Color dark = new Color(0f, 0f, 0f, 0.3f);
				Color cristal = new Color(1f, 1f, 1f, 0.0f);

				g2.setColor(dark);
				g2.fillRect(x, height, VERTICAL_MARGIN-INSTRUMENT_BORDER, (int) getBounds().getMaxY()+INSTRUMENT_BORDER);	
				
				g2.fillRect((int) getBounds().getMaxX()+VERTICAL_MARGIN+INSTRUMENT_BORDER , height, 
								VERTICAL_MARGIN-INSTRUMENT_BORDER, (int) getBounds().getMaxY()+INSTRUMENT_BORDER);	
				
				GradientPaint gp = new GradientPaint(x, y , cristal, 0f,  height, dark);  
				g2.setPaint(gp);  
				g2.fillRect(x, y, width, height);		
				
				y = (int) getBounds().getMaxY() +height;
				height = SHADOW_HEIGHT;
				
				gp = new GradientPaint(x, y , dark, x,  y+height, cristal);  
				g2.setPaint(gp);  
				g2.fillRect(x, y+INSTRUMENT_BORDER, width, height);	
				
				
			}
		}
		
		private void resizeInstrumentPanel() {
			_toolbar.resizeToolbar();
			int width = _instrumentsContainer.getWidth() - VERTICAL_MARGIN*2;
			Dimension size = new Dimension(width, _instrument.defaultHeight());
			setMinimumSize(size);
			setPreferredSize(size);
			setSize(size);
		}

		@Override public JPopupMenu actions() { return _toolbar._menuActions; }
		@Override public Container contentPane() {	return this; }
	}
	
	private class InstrumentInstaller{
		private InstrumentPanel install(final Instrument instrument) {
			
			final InstrumentPanelImpl instrumentPanel = new InstrumentPanelImpl(instrument);
			
			my(GuiThread.class).strictInvokeAndWait(new Runnable(){	
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