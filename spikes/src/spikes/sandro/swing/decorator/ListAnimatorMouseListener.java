package spikes.sandro.swing.decorator;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class ListAnimatorMouseListener extends MouseAdapter implements MouseMotionListener {
	
	private ListAnimatorDecorator _animator;
	private boolean _dragActive;
	private Point _origin;

	public ListAnimatorMouseListener(ListAnimatorDecorator animator) {
		this._animator = animator;
	}

	private boolean sufficientMove(Point where) {
		int dx = Math.abs(_origin.x - where.x);
		int dy = Math.abs(_origin.y - where.y);
		return Math.sqrt(dx * dx + dy * dy) > 5;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		_origin = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (_dragActive) {
			_animator.endDrag(e.getPoint());
			_dragActive = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!_dragActive) {
			if (sufficientMove(e.getPoint())) {
				_animator.startDrag(_origin);
				_dragActive = true;
			}
		}
		if (_dragActive)
			_animator.setInsertionLocation(e.getPoint());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (_dragActive)
			_animator.setInsertionIndex(-1);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (_dragActive)
			_animator.setInsertionLocation(e.getPoint());
	}
}
