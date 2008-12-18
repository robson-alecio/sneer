package snapps.watchme.gui.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import snapps.watchme.WatchMe;
import snapps.watchme.gui.WatchMeGui;
import sneer.skin.snappmanager.InstrumentManager;
import static wheel.lang.Environments.my;

class WatchMeGuiImpl implements WatchMeGui{ //Optimize need a better snapp window support

	private final InstrumentManager _instrumentManager = my(InstrumentManager.class);
	
	private final WatchMe _watchMe = my(WatchMe.class);
	
	JToggleButton _watchMeButton;
	
	WatchMeGuiImpl() {
		_instrumentManager.registerInstrument(this);
	}

	private ImageIcon loadIcon(String fileName) {
		try {
			return new ImageIcon(ImageIO.read(this.getClass().getResource(fileName)));
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	@Override
	public void init(Container container) {
		container.setBackground(Color.WHITE);
		container.setLayout(new FlowLayout());
		_watchMeButton = createButton(container, "Watch Me!");
		createWatchMeButtonListener();
	}

	@Override
	public int defaultHeight() {
		return ANY_HEIGHT;
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

	private JToggleButton createButton(Container container, String tip) {
		final JToggleButton btn = new JToggleButton();
		btn.setPreferredSize(new Dimension(40,40));
		btn.setBorder(new EmptyBorder(2,2,2,2));
		btn.setOpaque(true);
		btn.setBackground(Color.WHITE);
		btn.setToolTipText(tip);
		addMouseListener(btn);
		container.add(btn);
		return btn;
	}
	
	private void addMouseListener(final JToggleButton btn) {
		btn.addMouseListener(new MouseAdapter() {
			Icon WATCHME_ON = loadIcon("watchMeOn.png");
			Icon WATCHME_OFF = loadIcon("watchMeOff.png");
			{btn.setIcon(WATCHME_OFF);}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setIcon(WATCHME_ON);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mouseReleased(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (btn.isSelected())
					btn.setIcon(WATCHME_ON);
				else
					btn.setIcon(WATCHME_OFF);
			}
		});
	}
}