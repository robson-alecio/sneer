package snapps.watchme.gui.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import snapps.watchme.WatchMe;
import snapps.watchme.gui.WatchMeGui;
import sneer.kernel.container.Inject;
import sneer.skin.image.ImageFactory;
import sneer.skin.snappmanager.InstrumentManager;

class WatchMeGuiImpl implements WatchMeGui{ //Optimize need a better snapp window support

	@Inject
	static private InstrumentManager _instrumentManager;
	
	@Inject
	static private ImageFactory _imageFactory;

	@Inject
	static private WatchMe _watchMe;
	
	JToggleButton _watchMeButton;
	
	private final ImageIcon WATCHME_ON;
	private final ImageIcon WATCHME_OFF;

	WatchMeGuiImpl() {
		_instrumentManager.registerInstrument(this);
		WATCHME_ON = loadIcon("watchMeOn.png");
		WATCHME_OFF = loadIcon("watchMeOff.png");
	}

	private ImageIcon loadIcon(String fileName) {
		return _imageFactory.getIcon(this.getClass(), fileName);
	}

	public void init(Container container) {
		container.setBackground(Color.WHITE);
		container.setLayout(new FlowLayout());
		_watchMeButton = createButton(container, WATCHME_ON, WATCHME_OFF, 	"Watch Me!");
		createWatchMeButtonListener();
	}

	private void createWatchMeButtonListener() {
		_watchMeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (_watchMeButton.isSelected())
					_watchMe.startShowingMyScreen();
				else
					_watchMe.stopShowingMyScreen();
			}
		});
	}

	private JToggleButton createButton(Container container, final Icon onIcon, final Icon offIcon, String tip) {
		final JToggleButton btn = new JToggleButton(offIcon);
		btn.setPreferredSize(new Dimension(40,40));
		btn.setBorder(new EmptyBorder(2,2,2,2));
		btn.setOpaque(true);
		btn.setBackground(Color.WHITE);
		btn.setToolTipText(tip);
		addMouseListener(onIcon, offIcon, btn);
		container.add(btn);
		return btn;
	}
	
	private void addMouseListener(final Icon onIcon, final Icon offIcon, final JToggleButton btn) {
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setIcon(onIcon);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mouseReleased(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (btn.isSelected())
					btn.setIcon(onIcon);
				else
					btn.setIcon(offIcon);
			}
		});
	}
}