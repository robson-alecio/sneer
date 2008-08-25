package sneer.skin.widgets.reactive.impl;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.swing.JList;
import javax.swing.ListCellRenderer;


public abstract class ListAnimatorDecorator extends AbstractComponentDecorator {

    private static final int INTERVAL = 1000 / 24;
    private static Timer timer = new Timer(true);

    private final class GhostedDragImage extends AbstractComponentDecorator {
        private int index;
        private Point location;
        private Point offset;
        
        public GhostedDragImage(int cellIndex, Point origin) {
            super(_list);
            this.index = cellIndex;
            Rectangle b = _list.getCellBounds(index, index);
            location = origin;
            this.offset = new Point(0, origin.y - b.y);
        }
        
        public void setLocation(Point where) {
            this.location = where;
            getPainter().repaint();
        }
        
        @Override
		public void paint(Graphics g) {
            Rectangle b = _list.getCellBounds(index, index);
            Point origin = new Point(0, location.y-offset.y);
            origin.y = Math.max(0, origin.y);
            origin.y = Math.min(origin.y, _list.getHeight() - b.height);
            g = g.create(origin.x, origin.y, b.width, b.height);
            ((Graphics2D)g).translate(-b.x, -b.y);
            ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            _list.paint(g);
        }
    }
    
    private final class Counter extends TimerTask {
        @Override
		public void run() {
            synchronized(ListAnimatorDecorator.this) {
                if (reposition()) {
                    repaint();
                }
            }
        }
    }
    
    private Counter counter;
    private int insertionIndex = -1;
    private int draggedIndex = -1;
    private JList _list;
    private Map<Integer, Rectangle> bounds = new TreeMap<Integer, Rectangle>();
    private GhostedDragImage dragImage;
    private Point _origin;
    
    public ListAnimatorDecorator(final JList list) {
        super(list);
        _list = list;
        counter = new Counter();
        timer.schedule(counter, INTERVAL, INTERVAL);
    }
    protected Object getPlaceholder() { return ""; }
    protected abstract void move(int fromIndex, int toIndex);
    protected void drop(@SuppressWarnings("unused")	Transferable t, 
    					@SuppressWarnings("unused") int index) { 
    	//ignore
    } 
    
    private void initialize(Point where) {
        insertionIndex = draggedIndex = -1;
        _origin = where;
        int size = _list.getModel().getSize();
        for (int i=0;i < size;i++) {
            bounds.put(new Integer(i), getCellBoundsAfterInsertion(i));
        }
    }
    /** Track a drag which originated somewhere else. */
    public synchronized void startDragOver(Point where) {
        initialize(where);
        insertionIndex = getIndex(where, false);
    }
    /** Stop tracking an external drag. */
    public synchronized void endDragOver(Point where, Transferable t) {
        int idx = getIndex(where, false);
        if (idx != -1) {
            drop(t, idx);
        }
    }
    /** Start an internal drag. */
    public synchronized void startDrag(Point where) {
        initialize(where);
        draggedIndex = insertionIndex = getIndex(where, true);
        dragImage = new GhostedDragImage(draggedIndex, _origin);
    }
    /** End an internal drag. */
    public synchronized void endDrag(Point where) {
        int toIndex = getIndex(where, true);
        int fromIndex = draggedIndex;
        dragImage.dispose();
        dragImage = null;
        draggedIndex = insertionIndex = -1;
        if (toIndex != -1 && toIndex != fromIndex) {
            Map<Integer, Rectangle> newBounds = new TreeMap<Integer, Rectangle>();
            newBounds.put(new Integer(toIndex),
                          bounds.get(new Integer(fromIndex)));
            if (fromIndex < toIndex) {
                for (int i=fromIndex+1;i <= toIndex;i++) {
                    newBounds.put(new Integer(i-1),
                                  bounds.get(new Integer(i)));
                }
            }
            else {
                for (int i=toIndex;i < fromIndex;i++) {
                    newBounds.put(new Integer(i+1),
                                  bounds.get(new Integer(i)));
                }
            }
            bounds.putAll(newBounds);
            move(fromIndex, toIndex);
        }
    }
    private boolean reposition() {
        boolean changed = false;
        for (Iterator<Integer> i=bounds.keySet().iterator();i.hasNext();) {
            Integer x = i.next();
            Rectangle current = getCurrentCellBounds(x.intValue());
            Rectangle end = getCellBoundsAfterInsertion(x.intValue());
            if (current.x != end.x || current.y != end.y) {
                int xdelta = (end.x - current.x)/2;
                int ydelta = (end.y - current.y)/2;
                if (xdelta == 0)
                    current.x = end.x;
                else
                    current.x += xdelta;
                if (ydelta == 0)
                    current.y = end.y;
                else
                    current.y += ydelta;
                bounds.put(x, current);
                changed = true;
            }
        }
        return changed;
    }
    private int getIndex(Point where, boolean restrict) {
        int idx = _list.locationToIndex(where);
        if (!restrict) {
            int size = _list.getModel().getSize();
            // Assumes the list considers points below the last item
            // be within last item
            Rectangle last = _list.getCellBounds(size-1, size-1);
            if (idx == size-1 && where.y > last.y + last.height) {
                idx = size;
            }
        }
        return idx;
    }
    public synchronized void setInsertionLocation(Point where) {
        // Avoid painting focus and/or selection bgs, kind of a hack
        getPainter().requestFocus();
        _list.clearSelection();
        setInsertionIndex(getIndex(where, draggedIndex != -1));
        dragImage.setLocation(where);
    }
    public synchronized void setInsertionIndex(int idx) {
        if (idx != insertionIndex) {
            insertionIndex = idx;
            repaint();
        }
    }
    private Rectangle getCellBoundsAfterInsertion(int index) {
        Rectangle r = _list.getCellBounds(index, index);
        if (draggedIndex != -1) {
            if (index > draggedIndex) {
                if (index <= insertionIndex) {
                    Rectangle r2 = _list.getCellBounds(draggedIndex, draggedIndex);
                    r.y -= r2.height;
                }
            }
            else if (index < draggedIndex) {
                if (index >= insertionIndex) {
                    Rectangle r2 = _list.getCellBounds(draggedIndex, draggedIndex);
                    r.y += r2.height;
                }
            }
            else {
                Rectangle r2 = _list.getCellBounds(insertionIndex, insertionIndex);
                r.y = r2.y;
            }
        }
        else if (insertionIndex != -1 && index > insertionIndex) {
            ListCellRenderer rnd = _list.getCellRenderer();
            Component c = rnd.getListCellRendererComponent(_list, getPlaceholder(), insertionIndex, false, false);
            r.y += c.getHeight();
        }
        return r;
    }
    private Rectangle getCurrentCellBounds(int cellIndex) {
        Rectangle r = getCellBoundsAfterInsertion(cellIndex);
        Rectangle r2 = bounds.get(new Integer(cellIndex));
        if (r2 != null) {
            r.x = r2.x;
            r.y = r2.y;
        }
        return r;
    }
    
    @Override
	public synchronized void paint(Graphics g) {
        boolean db = _list.isDoubleBuffered();
        _list.setDoubleBuffered(false);
        try { 
            Rectangle b = getDecorationBounds();
            g.setColor(_list.getBackground());
            g.fillRect(b.x, b.y, b.width, b.height);
            for (int i=0;i < _list.getModel().getSize();i++) {
                if (i == draggedIndex)
                    continue;
                Rectangle r = getCurrentCellBounds(i);
                Graphics g2 = g.create(r.x, r.y, r.width, r.height);
                Rectangle r2 = _list.getCellBounds(i, i);
                ((Graphics2D)g2).translate(0, -r2.y);
                _list.paint(g2);
            }
        }
        finally {
            _list.setDoubleBuffered(db);
        }
    }
}