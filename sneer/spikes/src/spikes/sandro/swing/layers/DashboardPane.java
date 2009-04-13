package spikes.sandro.swing.layers;

//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.GridLayout;
//import java.awt.Point;
//import java.awt.event.ComponentAdapter;
//import java.awt.event.ComponentEvent;
//import java.awt.event.MouseEvent;
//
//import javax.swing.BoxLayout;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLayeredPane;
//import javax.swing.JPanel;
//import javax.swing.border.LineBorder;

//import org.jdesktop.jxlayer.JXLayer;
//import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

//Window
//- Rootpane
//		- ContentPane - Dashbord (yellow)
// 		- layerpane (layout instrument)
//    			- instrument pane (red)         
//    			- toolbar (black)
//				- mouse listener
//    			- fog

//public class DashboardPane extends JPanel {

//	private final JLayeredPane _layeredPane = new JLayeredPane();
//	private final JPanel _instrumentPanel = new JPanel();
//	
//	private final int _margin = 10;
//	private final int _toolbarHeight = 20;
//
//	public DashboardPane()    {
//		
//		setBackground(Color.YELLOW);
//    	setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
//    	add(_layeredPane);
//    	
//    	addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
//    		resizePanels();
//		}});
//
//        _layeredPane.add(_instrumentPanel);
//        _instrumentPanel.setOpaque(true);
//        _instrumentPanel.setBackground(Color.RED);
//        
//        addSomeFakeInstruments(_instrumentPanel);
//    }
//
//	private void resizePanels() {
//		int x = _margin;
//		int y = _toolbarHeight;
//		int width = getSize().width - _margin*2;
//		int height = getSize().height - _margin - _toolbarHeight;
//		_instrumentPanel.setBounds(x, y, width, height);
//	}
//
//	private void addSomeFakeInstruments(JPanel instrumentPanel) {
//		instrumentPanel.setLayout(new GridLayout(3,2,0,1));
//        new FakeInstrument(instrumentPanel);
//        new FakeInstrument(instrumentPanel);
//        new FakeInstrument(instrumentPanel);
//	}
//    
//    class FakeInstrument extends JPanel{
//    	private final JPanel _toolbar = new JPanel();
//		private AbstractLayerUI<JPanel> _instrumentGlassPane;
//		private final JXLayer<JPanel> _instrumentLayer ;
//    	
//    	FakeInstrument(JPanel instrumentPanel) {
//			_instrumentGlassPane = new AbstractLayerUI<JPanel>() {
//				@Override
//				protected void paintLayer(Graphics2D g2, JXLayer<JPanel> l) {
//					super.paintLayer(g2, l);
//					g2.setColor(new Color(0, 100, 0, 100));
//					g2.fillRect(0, 0, l.getWidth(), l.getHeight());
//				}
//
//				@Override
//				protected void processMouseEvent(MouseEvent event, 	JXLayer<JPanel> layer) {
//					_toolbar.setVisible(true);
//					if(event.getID()==MouseEvent.MOUSE_EXITED)
//						_toolbar.setVisible(false);
//				}
//			};
//			_instrumentLayer = new JXLayer<JPanel>(this, _instrumentGlassPane);
//			initGui();
//			instrumentPanel.add(_instrumentLayer);
//		}
//
//    	private void initGui() {
//    		_toolbar.setVisible(false);
//        	setLayout(new BorderLayout());
//        	add(new JButton("Teste"), BorderLayout.CENTER);
//        	
//        	_toolbar.setOpaque(false);
//            _toolbar.setBorder(new LineBorder(Color.BLACK));
//            _layeredPane.add(_toolbar, new Integer(1), 0);
//            
//            
//        	addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
//        		resizeToolbar();
//    		}});
//    	}
//
//		private void resizeToolbar() {
//			Point layeredPanePoint = _layeredPane.getLocationOnScreen();
//    		Point instrumentPoint = getLocationOnScreen();
//    		
//    		int x = _margin;
//    		int y = instrumentPoint.y - layeredPanePoint.y - _toolbarHeight;
//    		int width = getSize().width;
//    		int height = _toolbarHeight;
//    		_toolbar.setBounds(x, y, width, height);
//		}
//    }    
//    
//    private static void createAndShowGUI() {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        frame.setContentPane(new DashboardPane());
//        frame.setBounds(10, 10, 300, 600);
//        frame.setVisible(true);
//    }
//    
//    public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() { public void run() {
//        	createAndShowGUI();
//        }});
//    }
//}