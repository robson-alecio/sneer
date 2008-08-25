package sneer.skin.widgets.reactive.impl;

import static wheel.lang.Types.cast;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;


public class ListAnimatorBackgroundPainter extends AbstractComponentDecorator {
	public static String key(int layer) {
		return "backgroundPainter for layer " + layer;
	}

	private String key;
	private int _layer;
	

	public ListAnimatorBackgroundPainter(JLayeredPane p, int layer) {
		super(p, 0, TOP);
		setLayer(layer);
		key = key(layer);
		p.putClientProperty(key, this);
	}

	private int hideChildren(Container c) {
		if (c == null)
			return 0;
		int value = c.getComponentCount();
		try {
			AbstractComponentDecorator.nComponents.set(c, new Integer(0));
		} catch (Exception e) {
			return c.getComponentCount();
		}
		return value;
	}

	private void restoreChildren(Container c, int count) {
		if (c != null) {
			try {
				nComponents.set(c, new Integer(count));
			} catch (Exception e) {
			}
		}
	}

	private void paintBackground(Graphics g, 
			@SuppressWarnings("unused")	Component parent, JComponent jc) {
		int x = jc.getX();
		int y = jc.getY();
		int w = jc.getWidth();
		int h = jc.getHeight();
		paintBackground(g.create(x, y, w, h), jc);
	}

	private void paintBackground(Graphics g, JComponent jc) {
		if (jc.isOpaque()) {
			if (AbstractComponentDecorator.useSimpleBackground()) {
				g.setColor(jc.getBackground());
				g.fillRect(0, 0, jc.getWidth(), jc.getHeight());
			} else {
				int count = hideChildren(jc);
				boolean db = jc.isDoubleBuffered();
				if (db)
					jc.setDoubleBuffered(false);
				jc.paint(g);
				if (db)
					jc.setDoubleBuffered(true);
				restoreChildren(jc, count);
			}
		}
		Component[] kids = jc.getComponents();
		for (int i = 0; i < kids.length; i++) {
			if (kids[i] instanceof JComponent) {
				paintBackground(g, jc, (JComponent) kids[i]);
			}
		}
	}

	private List<Container> findOpaque(Component root) {
		List<Container> list = new ArrayList<Container>();
		if (root.isOpaque() && root instanceof JComponent) {
			list.add((Container) root);
			((JComponent) root).setOpaque(false);
		}
		if (root instanceof Container) {
			Component[] kids = ((Container) root).getComponents();
			for (int i = 0; i < kids.length; i++) {
				list.addAll(findOpaque(kids[i]));
			}
		}
		return list;
	}

	private List<Container> findDoubleBuffered(Component root) {
		List<Container> list = new ArrayList<Container>();
		if (root.isDoubleBuffered() && root instanceof JComponent) {
			list.add((Container) root);
			((JComponent) root).setDoubleBuffered(false);
		}
		if (root instanceof Container) {
			Component[] kids = ((Container) root).getComponents();
			for (int i = 0; i < kids.length; i++) {
				list.addAll(findDoubleBuffered(kids[i]));
			}
		}
		return list;
	}

	private void paintForeground(Graphics g, JComponent jc) {
		List<JComponent> opaque = cast(findOpaque(jc));
		List<JComponent> db = cast(findDoubleBuffered(jc));
		jc.paint(g);
		for (Iterator<JComponent> i = opaque.iterator(); i.hasNext();) {
			i.next().setOpaque(true);
		}
		for (Iterator<JComponent> i = db.iterator(); i.hasNext();) {
			i.next().setDoubleBuffered(true);
		}
	}

	@Override
	public void paint(Graphics g) {

		JLayeredPane lp = (JLayeredPane) getComponent();
		Component[] kids = lp.getComponents();
		// Construct an area of the intersection of all decorators
		Area area = new Area();
		List<ListAnimatorPainter> painters = new ArrayList<ListAnimatorPainter>();
		List<JComponent> components = new ArrayList<JComponent>();
		for (int i = kids.length - 1; i >= 0; i--) {
			if (kids[i] instanceof ListAnimatorPainter) {
				ListAnimatorPainter p = (ListAnimatorPainter) kids[i];
				if (p.isBackgroundDecoration()
						&& p.getDecoratedLayer() == getLayer() && p.isShowing()) {
					painters.add(p);
					area.add(new Area(p.getBounds()));
				}
			} else if (lp.getLayer(kids[i]) == getLayer()
					&& kids[i] instanceof JComponent) {
				components.add((JComponent) kids[i]);
			}
		}
		if (painters.size() == 0) {
			dispose();
			return;
		}
		g.setClip(area);

		// Paint background for that area
		for (Iterator<JComponent> i = cast(components.iterator()); i.hasNext();) {
			JComponent c = i.next();
			paintBackground(g, lp, c);
		}

		// Paint the bg decorators
		for (Iterator<ListAnimatorPainter> i = cast(painters.iterator()); i.hasNext();) {
			ListAnimatorPainter p = i.next();
			p.paint(g.create(p.getX(), p.getY(), p.getWidth(), p.getHeight()));
		}

		// Paint foreground for the area
		for (Iterator<JComponent> i = cast(components.iterator()); i.hasNext();) {
			JComponent c = i.next();
			paintForeground(g.create(c.getX(), c.getY(), c.getWidth(), c
					.getHeight()), c);
		}
	}

	@Override
	public void dispose() {
		getComponent().putClientProperty(key, null);
		super.dispose();
	}

	@Override
	public String toString() {
		return key + " on " + getComponent();
	}

	public void setLayer(int layer) {
		_layer = layer;
	}

	public int getLayer() {
		return _layer;
	}
}