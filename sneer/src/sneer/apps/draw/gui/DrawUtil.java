package sneer.apps.draw.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;

public class DrawUtil {

	public static JComboBox colorBox() {
		Object[] data = { new ColoredObject(Color.BLACK, "Black"),
				new ColoredObject(Color.BLUE, "Blue"),
				new ColoredObject(Color.CYAN, "Cyan"),
				new ColoredObject(Color.RED, "Red"),
				new ColoredObject(Color.GREEN, "Green"),
				new ColoredObject(Color.ORANGE, "ORANGE"),
				new ColoredObject(Color.GRAY, "Gray"),
				new ColoredObject(Color.YELLOW, "Yellow"),
				new ColoredObject(Color.WHITE, "White") };
		JComboBox jcb = new JComboBox(data);
		prepareComponent(jcb);
		jcb.setRenderer(new ColoredRenderer());
		return jcb;
	}

	public static JComboBox sizeBox() {
		Object[] sizes = new Object[] { "5", "10", "25", "50" };
		JComboBox sizeBox = new JComboBox(sizes);
		DrawUtil.prepareComponent(sizeBox);
		return sizeBox;
	}

	public static void prepareComponent(JComponent component) {
		component.setMaximumSize(new Dimension(75, 20));
		component.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	public static class ColoredObject {
		Color _color;

		String _name;

		public ColoredObject(Color color, String name) {
			_color = color;
			_name = name;
		}

		@Override
		public String toString() {
			return _name;
		}
	}

	public static class ColoredRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean hasFocus) {
			Component c = super.getListCellRendererComponent(list, value,
					index, isSelected, hasFocus);
			if (!isSelected && !hasFocus && value instanceof ColoredObject)
				c.setBackground(((ColoredObject) value)._color);
			return c;
		}

		private static final long serialVersionUID = 1L;
	}

}
