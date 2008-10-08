package snapps.wind.gui.impl;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import snapps.wind.Shout;
import snapps.wind.Wind;
import snapps.wind.gui.WindGui;
import sneer.kernel.container.Inject;
import sneer.pulp.keymanager.KeyManager;
import sneer.skin.image.ImageFactory;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.graphics.Images;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;

class WindGuiImpl implements WindGui {
	
	@Inject
	static private InstrumentManager _instruments;

	@Inject
	static private Wind _wind;

	@Inject
	static private ReactiveWidgetFactory _rfactory;

	@Inject
	static private KeyManager _keyManager;
	
	@Inject
	static private ImageFactory _imageFactory;

	private final static Signal<Image> _meImage;
	private final static Signal<Image> _otherImage;
	
	private ListWidget<Shout> _shoutsList;
	private TextWidget<JTextField> _myShout;

	private Container _container;

	private JToggleButton _lock;

	static {
		_meImage = new Constant<Image>(loadImage("me.png"));
		_otherImage = new Constant<Image>(loadImage("other.png"));
	}
	
	public WindGuiImpl() {
		_instruments.registerInstrument(this);
	} 
	
	private static Image loadImage(String fileName) {
		return Images.getImage(WindGuiImpl.class.getResource(fileName));
	}

	private Signal<Image> image(Shout shout) {
		return shout.publisher == _keyManager.ownPublicKey()
			? _meImage
			: _otherImage;
	}	

	@Override
	public void init(Container container) {
		_container = container;
		ShoutLabelProvider labelProvider = new ShoutLabelProvider();
		_shoutsList = _rfactory.newList(_wind.shoutsHeard(), labelProvider, new WindListCellRenderer(labelProvider));
		_myShout = _rfactory.newTextField(new Constant<String>(""), _wind.megaphone(), true);
		_container.setBackground(_shoutsList.getComponent().getBackground());
		iniGui();
	}

	private void iniGui() {
		JScrollPane scrollPane = createScrollPane();
		scrollPane.getViewport().add(_shoutsList.getComponent());
		
		initAutoScroll();
		
		_container.setLayout(new GridBagLayout());
		_container.add(scrollPane, new GridBagConstraints(0, 0, 2, 1, 1., 1.,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		_container.add(_myShout.getComponent(), new GridBagConstraints(0, 1, 1, 1, 1., 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 5, 5, 0), 0, 0));

		_container.add(_lock, new GridBagConstraints(1, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 5, 5, 5), 0, 0));		
		
		_shoutsList.getComponent().setBorder(new EmptyBorder(0,0,0,0));
	}

	private void initAutoScroll() {
		_lock = new JToggleButton();
		_lock.setPreferredSize(new Dimension(20,20));
		_lock.setBorder(new EmptyBorder(0,0,0,0));
		_lock.getModel().addChangeListener(new ChangeListener(){
			
			private final ImageIcon LOCK_ICON = _imageFactory.getIcon(WindGuiImpl.class, "lock.png");
			private final ImageIcon UNLOCK_ICON = _imageFactory.getIcon(WindGuiImpl.class, "unlock.png");
			{
				_lock.setIcon(UNLOCK_ICON);
			}
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if(_lock.isSelected())
					_lock.setIcon(LOCK_ICON);
				else
					_lock.setIcon(UNLOCK_ICON);
			}});
	}

	private JScrollPane createScrollPane() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(size(_container));
		scrollPane.setPreferredSize(size(_container));
		scrollPane.setBorder(new TitledBorder(new EmptyBorder(5,5,2,2), getName()));
		scrollPane.setOpaque(false);
		addAutoScrollListener(scrollPane);
		return scrollPane;
	}

	private void addAutoScrollListener(JScrollPane scrollPane) {//Optimize better auto-scroll
		final BoundedRangeModel model = scrollPane.getVerticalScrollBar().getModel();
		model.addChangeListener(new ChangeListener(){@Override	public void stateChanged(ChangeEvent e) {
			if(!_lock.isSelected())
				model.setValue(model.getMaximum());
		}});
	}

	private Dimension size(Container container) {
		return new Dimension(container.getSize().width, 298 );
	}
	
	private String getName() {
		return "Wind";
	}

	final class ShoutLabelProvider implements LabelProvider<Shout> {
				
		@Override
		public Signal<String> labelFor(Shout shout) {
			return new Constant<String>(shout.phrase);
		}

		@Override
		public Signal<Image> imageFor(Shout shout) {
			return image(shout);
		}
	}
}