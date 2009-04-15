package sneer.skin.old.dashboard.impl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class OldGlassPane extends JPanel implements MouseListener, MouseMotionListener, FocusListener {

	Container _contentPane;
	Container _toolbar;

	public OldGlassPane(Container contentPane, JPanel toolbar) {
		_contentPane = contentPane;
		_toolbar = toolbar;
		setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
		addFocusListener(this);
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible)	requestFocus();
		super.setVisible(visible);
	}
	
	@Override public void focusLost(FocusEvent fe) { if (isVisible())  requestFocus(); }
	@Override public void focusGained(FocusEvent fe) {/*ignore*/}
	@Override public void mouseDragged(MouseEvent e) { redispatchMouseEvent(e);	}
	@Override public void mouseMoved(MouseEvent e) { redispatchMouseEvent(e);	}
	@Override public void mouseClicked(MouseEvent e) { redispatchMouseEvent(e);	}
	@Override public void mouseEntered(MouseEvent e) { redispatchMouseEvent(e);}
	@Override public void mouseExited(MouseEvent e) { redispatchMouseEvent(e);	}
	@Override public void mousePressed(MouseEvent e) { redispatchMouseEvent(e);}
	@Override public void mouseReleased(MouseEvent e) { redispatchMouseEvent(e);	}

	private void redispatchMouseEvent(MouseEvent e) {
		Point glassPanePoint = e.getPoint();
		Component component = null;
		
		Point containerPoint = SwingUtilities.convertPoint(this,	glassPanePoint, _toolbar);
		component = SwingUtilities.getDeepestComponentAt(_toolbar,	containerPoint.x, containerPoint.y);
		
		if (component == null){
			containerPoint = SwingUtilities.convertPoint(this,	glassPanePoint, _contentPane);
			component = SwingUtilities. getDeepestComponentAt(_contentPane,	containerPoint.x, containerPoint.y);
		}
		
		if (component == null) return;

		Point componentPoint = SwingUtilities.convertPoint(this, glassPanePoint, component);
		component.dispatchEvent(new MouseEvent(component, e.getID(), e.getWhen(), e.getModifiers(), 
											componentPoint.x, componentPoint.y, e.getClickCount(), e.isPopupTrigger()));
	}
}