package snapps.wind.gui.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import snapps.wind.Shout;
import snapps.wind.Wind;
import snapps.wind.gui.WindGui;
import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
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

	private void showMessage(Light light){
		LightType type = light.type();
		String title = type.name();
		int optType = type==LightType.ERROR?JOptionPane.ERROR_MESSAGE: 
					  type==LightType.WARN? JOptionPane.WARNING_MESSAGE:
						  				 JOptionPane.INFORMATION_MESSAGE;
		
		JOptionPane.showMessageDialog(_container, createMessage(light), title, optType);
	}

	private String createMessage(Light light) {
		String stack = "";
		String msg = "";
		
		if(light.error()!=null){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(out);
			light.error().printStackTrace(ps);
			stack = "\n " + new String(out.toByteArray());
			ps.close();
			msg = light.error().getMessage();
		}
		
		msg = msg==null?"":msg;
		
		return light.message() + "\n " + msg + stack;
	}	
	@Override
	public void init(Container container) {
		_container = container;
		_shoutsList = _rfactory.newList(_wind.shoutsHeard(), new ShoutLabelProvider());
		_myShout = _rfactory.newTextField(new Constant<String>(""), _wind.megaphone(), true);
		
		iniGui();
		initMouseListener();
	}

	private void iniGui() {
		JScrollPane scrollPane = new JScrollPane();
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
	
	private void initMouseListener() {
		_shoutsList.getComponent().addMouseListener(new MouseAdapter(){ @Override public void mouseReleased(final MouseEvent event) {
			Light light = getClickedLight(event);
			showMessage(light);
		}
		
		private Light getClickedLight(final MouseEvent event) {
			JList list = (JList)event.getSource();
			list.setSelectedIndex(list.locationToIndex(event.getPoint()));
			Light light = (Light)list.getSelectedValue();
			return light;
		}});
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
