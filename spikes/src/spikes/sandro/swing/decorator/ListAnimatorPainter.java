package spikes.sandro.swing.decorator;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;


public class ListAnimatorPainter extends JComponent {
	private static final long serialVersionUID = 1L;
	private int _base;
	private Cursor _cursor;
	
	private final AbstractComponentDecorator _decorator;
	{
		setFocusable(false);
	}

	public ListAnimatorPainter(AbstractComponentDecorator decorator){
		_decorator = decorator;
	}
	
	public JComponent getComponent() {
		return _decorator.getComponent();
	}

	public void setDecoratedLayer(int base) {
		_base = base;
	}

	public int getDecoratedLayer() {
		return _base;
	}

	public boolean isBackgroundDecoration() {
		return _decorator.getLayerOffset() < 0;
	}

	@Override
	public void setCursor(Cursor cursor) {
		Cursor oldCursor = getCursor();
		if (oldCursor == null && cursor != null || oldCursor != null
				&& !oldCursor.equals(cursor)) {
			_cursor = cursor;
			super.setCursor(cursor);
		}
	}

	@Override
	public Cursor getCursor() {
		return _cursor != null ? _cursor : _decorator.getComponent().getCursor();
	}

	@Override
	public void paintComponent(Graphics g) {
		if (!_decorator.getComponent().isShowing())
			return;
		Graphics g2 = g.create();
		g2.translate(-_decorator.getOriginOffset().x, -_decorator.getOriginOffset().y);
		_decorator.paint(g2);
		g2.dispose();
	}

	@Override
	public String getToolTipText(MouseEvent e) {
		return _decorator.getToolTipText(e);
	}

	@Override
	public String toString() {
		return "Painter for " + _decorator;
	}
}