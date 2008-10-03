package snapps.wind.gui.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import snapps.wind.Shout;
import snapps.wind.Wind;
import snapps.wind.gui.WindGui;
import sneer.kernel.container.Inject;
import sneer.pulp.keymanager.KeyManager;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.graphics.Images;
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

	private final static Signal<Image> _meImage;
	private final static Signal<Image> _otherImage;
	
	private ListWidget<Shout> _shoutsList;
	private TextWidget<JTextField> _myShout;

	private Container _container;

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
		_shoutsList = _rfactory.newList(_wind.shoutsHeard(), new ShoutLabelProvider(), true);
		_shoutsList.setLineSpace(5);
		_myShout = _rfactory.newTextField(new Constant<String>(""), _wind.megaphone(), true);
		iniGui();
	}

	private void iniGui() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		_container.setLayout(new BorderLayout());
		_container.add(scrollPane, BorderLayout.CENTER);
		_container.add(_myShout.getComponent(), BorderLayout.SOUTH);
		
		scrollPane.getViewport().add(_shoutsList.getComponent());
		scrollPane.setMinimumSize(size(_container));
		scrollPane.setPreferredSize(size(_container));
		scrollPane.setBorder(new TitledBorder(new EmptyBorder(5,5,5,5), getName()));
		_shoutsList.getComponent().setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setBackground(_shoutsList.getComponent().getBackground());
	}

	private Dimension size(Container container) {
		return new Dimension(container.getSize().width, 300 );
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