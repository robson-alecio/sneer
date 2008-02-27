package spikes.sandro.swing.window.transparent;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

@SuppressWarnings("unchecked")
public class JWindow<CORE extends Core> extends javax.swing.JWindow {
	
	protected static final long serialVersionUID = 1L;
	protected CORE core;
	Point lastPress = null;
	boolean continueDrag = false;
	private String title = "";
	
	
	public JWindow(int titleH, String txt) {
		this(titleH);
		this.title = txt;
	}
	
	public JWindow(int titleH) {
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK|AWTEvent.FOCUS_EVENT_MASK);
		core = (CORE) new Core(titleH);
		init();
	}

	protected JWindow(CORE pcore, String txt) {
		this(pcore);
		this.title = txt;
	}
	
	protected JWindow(CORE pcore) {
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK|AWTEvent.FOCUS_EVENT_MASK);
		this.core = pcore;
		init();
	}

	protected void init() {
		this.setSize(new Dimension(300, 300));
		this.setGlassPane(core.newDefaultGlassPane());
		this.setContentPane(core.getCompositPane(title));
		this.getCore().getTransparentTitle(title).updateBackground();
		this.getCore().getTransparentPane().updateBackground();
	}
		
	@Override
	public Container getContentPane() {
		return core.getTransparentPane();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.getGlassPane().setVisible(visible);
	}
	
	public CORE getCore() {
		return core;
	}
	
	@Override
    protected void processMouseMotionEvent(MouseEvent e){
        super.processMouseMotionEvent(e);
        if(e.getID() == MouseEvent.MOUSE_DRAGGED){
            Point p = e.getPoint();
            if(continueDrag || this.core.getTransparentTitle(title).contains(p)){
	            SwingUtilities.convertPointToScreen(p, (Component)e.getSource());
	            p.x=p.x-5;
	            p.y=p.y-5;
	            setLocation(p.x, p.y);
	            repaint();
	            continueDrag=true;
            }
        }else {
        	continueDrag=false;
        }
    }
	
	public static void main(String[] args) {
		JWindow win = new JWindow(15, "Transparent Window");
		win.setBounds(10, 10, 400, 500);		
		win.setVisible(true);		
	}	
       
}
