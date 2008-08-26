package spikes.sandro.swing.decorator;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;


public abstract class AbstractComponentDecorator {

	public static final Rectangle DEFAULT_BOUNDS = null;
    public static final int TOP = 0;
    
    private Point _originOffset = new Point(0, 0);
    
    private ListAnimatorPainter _painter;
    JComponent _component;
    private Container _parent;
    private Component _layerRoot;
    private Listener _listener;
    private int _layerOffset;
    private int _position;
    private Rectangle _bounds;

    public AbstractComponentDecorator(JComponent c) {
        this(c, 1);
    }

    public AbstractComponentDecorator(JComponent c, int offset) {
        this(c, offset, TOP);
    }

    public AbstractComponentDecorator(JComponent c, int offset, int positon) {
        _component = c;
        this.setLayerOffset(offset);
        _position = positon;
        this._bounds = DEFAULT_BOUNDS;
        _parent = c.getParent();
        _painter = new ListAnimatorPainter(this);
        _listener = new Listener();
        _component.addHierarchyListener(_listener);
        _component.addComponentListener(_listener);
        attach();
    }

    public void setToolTipText(String text) {
        _painter.setToolTipText(text);
    }
    
    public String getToolTipText() {
        return _painter.getToolTipText();
    }
    
    public String getToolTipText(@SuppressWarnings("unused") MouseEvent e) {
        return getToolTipText();
    }
    
    public boolean isVisible() {
        return _painter.isVisible();
    }
    
    public void setVisible(boolean visible) {
        _painter.setVisible(visible);
    }

    protected void attach() {
        if (_layerRoot != null) {
            _layerRoot.removePropertyChangeListener(_listener);
            _layerRoot = null;
        }
        RootPaneContainer rpc = (RootPaneContainer)
            SwingUtilities.getAncestorOfClass(RootPaneContainer.class, _component);
        if (rpc != null
            && SwingUtilities.isDescendingFrom(_component, rpc.getLayeredPane())) {
            JLayeredPane lp = rpc.getLayeredPane();
            Component layeredChild = _component;
            int layer = JLayeredPane.DRAG_LAYER.intValue();
            if (this instanceof ListAnimatorBackgroundPainter) {
                layer = ((ListAnimatorBackgroundPainter)this).getLayer();
                _painter.setDecoratedLayer(layer);
            }
            else if (layeredChild == lp) {
                // Is the drag layer the best layer to use when decorating
                // the layered pane?
                _painter.setDecoratedLayer(layer);
            }
            else {
                while (layeredChild.getParent() != lp) {
                    layeredChild = layeredChild.getParent();
                }
                int base = lp.getLayer(layeredChild);
                // NOTE: JLayeredPane doesn't properly repaint an overlapping
                // child when an obscured child calls repaint() if the two
                // are in the same layer, so we use the next-higher layer
                // instead of simply using a different position within the 
                // layer.
                layer = base + getLayerOffset();
                if (getLayerOffset() < 0) {
                    ListAnimatorBackgroundPainter bp = (ListAnimatorBackgroundPainter)
                    lp.getClientProperty(ListAnimatorBackgroundPainter.key(base));
                    if (bp == null) {
                        bp = new ListAnimatorBackgroundPainter(lp, base);
                    }
                }
                _painter.setDecoratedLayer(base);
                _layerRoot = layeredChild;
                _layerRoot.addPropertyChangeListener(_listener);
            }
            lp.add(_painter, new Integer(layer), _position);
        }
        else {
            // Always detach when the target component's window is null
            // or is not a suitable container,
            // otherwise we might prevent GC of the component
            Container parent = _painter.getParent();
            if (parent != null) {
                parent.remove(_painter);
            }
        }
        // Track size changes in the decorated component's parent
        if (_parent != null) {
            _parent.removeComponentListener(_listener);
        }
        _parent = _component.getParent();
        if (_parent != null) {
            _parent.addComponentListener(_listener);
        }
        synch();
    }

    protected void synch() {
        Container painterParent = _painter.getParent();
        if (painterParent != null) {
            Rectangle decorated = getDecorationBounds();
            Rectangle clipRect = clipDecorationBounds(decorated);

            Point pt = SwingUtilities.convertPoint(_component, clipRect.x, clipRect.y, 
                                                   painterParent);
            if (clipRect.width <= 0 || clipRect.height <= 0) {
                setPainterBounds(-1, -1, 0, 0);
                setVisible(false);
            }
            else {
                setPainterBounds(pt.x, pt.y, clipRect.width, clipRect.height);
                setVisible(true);
            }
            painterParent.repaint();
        }
    }

    protected Rectangle clipDecorationBounds(Rectangle decorated) {
        // Amount we have to translate the Graphics context
        getOriginOffset().x = decorated.x;
        getOriginOffset().y = decorated.y;
        // If the the component is obscured (by a viewport or some
        // other means), use the painter bounds to clip to the visible 
        // bounds.  Doing may change the actual origin, so adjust our
        // origin offset accordingly
        Rectangle visible = getClippingRect(_component, decorated);
        Rectangle clipRect = decorated.intersection(visible);
        if (decorated.x < visible.x)
            getOriginOffset().x += visible.x - decorated.x;
        if (decorated.y < visible.y)
            getOriginOffset().y += visible.y - decorated.y;
        return clipRect;
    }
    
    private Rectangle getClippingRect(Container component, Rectangle desired) {
        Rectangle visible = component instanceof JComponent
            ? ((JComponent)component).getVisibleRect() 
            : new Rectangle(0, 0, component.getWidth(), component.getHeight());
        Rectangle clip = new Rectangle(desired);
        if (desired.x >= visible.x && desired.y >= visible.y
            && desired.x + desired.width <= visible.x + visible.width
            && desired.y + desired.height <= visible.y + visible.height) {
            // desired rect is within the current clip rect
        }
        else if (component.getParent() != null) {
            // Only apply the clip if it is actually smaller than the 
            // component's visible area
            if (component != _painter.getParent()
                && (visible.x > 0 || visible.y > 0
                    || visible.width < component.getWidth()
                    || visible.height < component.getHeight())) {
                // Don't alter the original rectangle
                desired = new Rectangle(desired);
                desired.x = Math.max(desired.x, visible.x);
                desired.y = Math.max(desired.y, visible.y);
                desired.width = Math.min(desired.width,
                                         visible.x + visible.width - desired.x);
                desired.height = Math.min(desired.height,
                                          visible.y + visible.height - desired.y);
                
                // Check for clipping further up the hierarchy
                desired.x += component.getX();
                desired.y += component.getY();
                clip = getClippingRect(component.getParent(), desired);
                clip.x -= component.getX();
                clip.y -= component.getY();
            }
        }
        return clip;
    }

    protected Rectangle getDecorationBounds() {
        return _bounds != DEFAULT_BOUNDS 
            ? _bounds : new Rectangle(0, 0, _component.getWidth(), _component.getHeight());
    }
    
    public void setDecorationBounds(Rectangle bounds) {
        if (bounds == DEFAULT_BOUNDS) {
            this._bounds = bounds;
        }
        else {
            this._bounds = new Rectangle(bounds);
        }
        synch();
    }
    
    public void setDecorationBounds(int x, int y, int w, int h) {
        setDecorationBounds(new Rectangle(x, y, w, h));
    }
    
    protected void setPainterBounds(int x, int y, int w, int h) {
        _painter.setLocation(x, y);
        _painter.setSize(w, h);
        repaint();
    }
    
    public JComponent getComponent() { return _component; }
    protected JComponent getPainter() { return _painter; }
    
    public void setCursor(Cursor cursor) {
        _painter.setCursor(cursor);
    }

    public void repaint() {
        JLayeredPane p = (JLayeredPane)_painter.getParent();
        if (p != null) {
            p.repaint(_painter.getBounds());
        }
    }
    
    public void dispose() {
        _component.removeHierarchyListener(_listener);
        _component.removeComponentListener(_listener);
        if (_parent != null) {
            _parent.removeComponentListener(_listener);
            _parent = null;
        }
        if (_layerRoot != null) {
            _layerRoot.removePropertyChangeListener(_listener);
            _layerRoot = null;
        }
        Container painterParent = _painter.getParent();
        if (painterParent != null) {
            Rectangle bounds = _painter.getBounds();
            painterParent.remove(_painter);
            painterParent.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        _component.repaint();
        _component = null;
    }

    public abstract void paint(Graphics g);
    
    @Override
	public String toString() {
        return super.toString() + " on " + getComponent();
    }
    
    protected static Field nComponents;
    static {
        try {
            nComponents = Container.class.getDeclaredField("ncomponents");
            nComponents.setAccessible(true);
        }
        catch(Exception e) {
            nComponents = null;
        }
    }
    
    protected static boolean useSimpleBackground() {
        return nComponents == null;
    }

    public void setOriginOffset(Point originOffset) {
		_originOffset = originOffset;
	}

	public Point getOriginOffset() {
		return _originOffset;
	}

	public void setLayerOffset(int layerOffset) {
		_layerOffset = layerOffset;
	}

	public int getLayerOffset() {
		return _layerOffset;
	}

	private final class Listener extends ComponentAdapter implements HierarchyListener, PropertyChangeListener {

    	public void hierarchyChanged(HierarchyEvent e) {
            if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
                attach();}}
        
        public void propertyChange(PropertyChangeEvent e) {
            if (JLayeredPane.LAYER_PROPERTY.equals(e.getPropertyName())) {
                attach();}}
        
        @Override public void componentMoved(ComponentEvent e) { attach();}
        @Override public void componentResized(ComponentEvent e) { attach(); }
        @Override public void componentHidden(ComponentEvent e) {setVisible(false);}
        @Override public void componentShown(ComponentEvent e) {setVisible(true);}
    }
}