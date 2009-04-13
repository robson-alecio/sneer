package spikes.sandro.swing.layers;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class DashboardPane extends JPanel {

	private final JLayeredPane _layeredPane = new JLayeredPane();
	private final JPanel _instrumentPanel = new JPanel();
	
	private final int _margin = 10;
	private final int _toolbarHeight = 20;

	public DashboardPane()    {

    	setBackground(Color.YELLOW);
    	setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    	add(_layeredPane);
    	
    	addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
    		resizePanels();
		}});

        _layeredPane.add(_instrumentPanel);
        _instrumentPanel.setOpaque(true);
        _instrumentPanel.setBackground(Color.RED);
        
        addSomeFakeInstruments(_instrumentPanel);
    }

	private void resizePanels() {
		int x = _margin;
		int y = _toolbarHeight;
		int width = getSize().width - _margin*2;
		int height = getSize().height - _margin - _toolbarHeight;
		_instrumentPanel.setBounds(x, y, width, height);
	}

	private void addSomeFakeInstruments(JPanel instrumentPanel) {
		instrumentPanel.setLayout(new GridLayout(3,2,0,1));
        instrumentPanel.add(new FakeInstrument());
        instrumentPanel.add(new FakeInstrument());
        instrumentPanel.add(new FakeInstrument());
	}
    
    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setContentPane(new DashboardPane());
        frame.setBounds(10, 10, 320, 640);
        frame.setVisible(true);
    }

    
    class FakeInstrument extends JPanel{
    	JPanel _toolbar = new JPanel();
    	FakeInstrument(){
            createToolbar();
        	addComponentListener(new ComponentAdapter(){ @Override public void componentResized(ComponentEvent e) {
        		resizePanels();
    		}});
        	addMouseListenerToShowAndHideToolbar();
    	}

		private void addMouseListenerToShowAndHideToolbar() {
			//Fix: use glasspane.
			addMouseListener(new MouseAdapter(){
				{ _toolbar.setVisible(false); }
				@Override public void mouseEntered(MouseEvent e) {
					_toolbar.setVisible(true);
				}

				@Override public void mouseExited(MouseEvent e) {
					_toolbar.setVisible(false);
				}});
		}

    	private void createToolbar() {
            _toolbar.setOpaque(false);
            _toolbar.setBorder(new LineBorder(Color.BLACK));
            _layeredPane.add(_toolbar, new Integer(2), 0);
    	}

    	private void resizePanels() {
    		Point layeredPanePoint = _layeredPane.getLocationOnScreen();
    		Point instrumentPoint = getLocationOnScreen();
    		
    		int x = _margin;
    		int y = instrumentPoint.y - layeredPanePoint.y - _toolbarHeight;
    		int width = getSize().width;
    		int height = _toolbarHeight;
    		_toolbar.setBounds(x, y, width, height);
    	}
    }    
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}