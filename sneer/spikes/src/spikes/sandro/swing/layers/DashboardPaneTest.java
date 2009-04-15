package spikes.sandro.swing.layers;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

import spikes.sandro.swing.layers.DashboardPaneTest.InstrumentWindow.Toolbar;

// 	JFrame
//			RootPane
//				DashboardParne (ContentPane)
//					_instrumentsAndToolbarsLayeredPane (JLayeredPane)
//						_instrumentsPanel (0..n)
//							_instrumentLayer (decorator)
//								InstrumentWindow (instrument container)
//								_instrumentGlasspane (mouse listener)
//						_mouseBlockButton (hack to block mose events)
//						_toolbarPanel (0..1)
//				Glasspane
public class DashboardPaneTest extends JPanel {

	private final JLayeredPane _instrumentsAndToolbarsLayeredPane = new JLayeredPane();
	private final JPanel _instrumentsPanel = new JPanel();
	private List<InstrumentWindow> _instruments = new ArrayList<InstrumentWindow>();

	public DashboardPaneTest()    {
		
    	setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    	add(_instrumentsAndToolbarsLayeredPane, new Integer(0));
    	addInstrumentPanelResizer();
    	setOpaque(true);

        _instrumentsAndToolbarsLayeredPane.add(_instrumentsPanel);
        addSomeFakeInstruments(_instrumentsPanel);
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

	private void addSomeFakeInstruments(JPanel instrumentPanel) {
		instrumentPanel.setLayout(new GridLayout(3,2,2,2));
		_instruments.add(new InstrumentWindow(instrumentPanel));
		_instruments.add(new InstrumentWindow(instrumentPanel));
		_instruments.add(new InstrumentWindow(instrumentPanel));
	}
	
	class InstrumentWindow extends JPanel{
		private final AbstractLayerUI<JPanel> _instrumentGlasspane;
		private final JXLayer<JPanel> _instrumentLayer ;
		private final Toolbar _toolbar = new Toolbar();
		
		InstrumentWindow(JPanel instrumentPanel) {
			_instrumentGlasspane = new InstrumentGlasspane(_toolbar);
			_instrumentLayer = new JXLayer<JPanel>(this, _instrumentGlasspane);
			initGui();
			instrumentPanel.add(_instrumentLayer);
		}

		private void initGui() {
			_toolbar.setVisible(false);
	    	setLayout(new BorderLayout());
	    	JButton button = new JButton("Teste");
	    	button.setBorder(new EmptyBorder(0,0,0,0));
			add(button, BorderLayout.CENTER);
	    	addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
	    		_toolbar.resizeToolbar();
	    	}});
		}

		class Toolbar{
			private static final int _OFFSET = 0;
			private static final int _TOOLBAR_HEIGHT = 20;
			
			private final JPanel _toolbarPanel = new JPanel();
			private final JButton _mouseBlockButton = new JButton(); //Fix: Remove this hack used to block mouse 
																									//event dispatch to the instrument behind toolbar 
			private Toolbar(){
				DashboardPaneTest.this._instrumentsAndToolbarsLayeredPane.add(_mouseBlockButton, new Integer(1));
				DashboardPaneTest.this._instrumentsAndToolbarsLayeredPane.add(_toolbarPanel, new Integer(2));
			}

			private void setVisible(boolean isVisible) {  
				_toolbarPanel.setVisible(isVisible); 	
				_mouseBlockButton.setVisible(isVisible);
			}
			private boolean isVisible() { 	return _toolbarPanel.isVisible(); 	}
			
			private void resizeToolbar() {
				Point layeredPanePoint = DashboardPaneTest.this._instrumentsAndToolbarsLayeredPane.getLocationOnScreen();
				Point instrumentPoint = getLocationOnScreen();
				
				int x = 0;
				int y = instrumentPoint.y - layeredPanePoint.y - Toolbar._TOOLBAR_HEIGHT+_OFFSET;
				int width = getSize().width;
				int height = Toolbar._TOOLBAR_HEIGHT;
				_toolbarPanel.setBounds(x, y, width, height);
				_mouseBlockButton.setBounds(x, y, width, height);
			}

			private boolean isOverAnyToolbar(Point mousePoint) {
				for (InstrumentWindow instrument : _instruments) {
					Toolbar toolbar = instrument._toolbar;
					if(toolbar.isVisible())
						return getAreaOnScreen(instrument._toolbar._toolbarPanel).contains(mousePoint);
				}
				return false;
			}
	
			private void hideAndShow(Point mousePoint) {
				for (InstrumentWindow instrument : _instruments) 
					instrument._toolbar.setVisible(getAreaOnScreen(instrument).contains(mousePoint));
			}
	
			private Rectangle getAreaOnScreen(JComponent component) {
				return new Rectangle(component.getLocationOnScreen(), component.getSize());
			}
		}
	}
	
	class InstrumentGlasspane extends AbstractLayerUI<JPanel> {
		private final Toolbar _toolbar;
		public InstrumentGlasspane(Toolbar toolbar) {
			_toolbar = toolbar;
		}

		@Override protected void paintLayer(Graphics2D g2, JXLayer<JPanel> l) {
			super.paintLayer(g2, l);
		}

		@Override protected void processMouseMotionEvent(MouseEvent event, JXLayer<JPanel> layer) {
			Point mousePoint = event.getLocationOnScreen();
			if(_toolbar.isOverAnyToolbar(mousePoint)) 
				return;
			
			_toolbar.hideAndShow(mousePoint);
		}
	};
}