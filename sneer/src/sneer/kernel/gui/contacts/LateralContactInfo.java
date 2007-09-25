package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.io.ui.widgets.ReactiveIntegerField;
import wheel.io.ui.widgets.ReactiveJpgImageField;
import wheel.io.ui.widgets.ReactiveMemoField;
import wheel.io.ui.widgets.ReactiveTextField;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class LateralContactInfo extends JPanel{

	private final Consumer<Pair<ContactId, String>> _nickChanger;
	private final Contact _contact;
	private final User _user;
	private final Signal<Font> _font;

	public LateralContactInfo( User user, Contact contact, Consumer<Pair<ContactId, String>> nickChanger, Signal<Font> font){
		setLayout(new BorderLayout());
		_user = user;
		_contact = contact;
		_nickChanger = nickChanger;
		_font = font;
		add(contentPanel(),BorderLayout.NORTH);
		add(new JPanel(),BorderLayout.CENTER);
	}
	
	private JPanel contentPanel(){
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		
		//Refactor: think if picture should delegate to panel define its size.
		Dimension pictureFieldSize = new Dimension(100,100);
		
		//Refactor: choose reactivefield sizes and maintain font aspect ratio
		content.add(new ReactiveJpgImageField(_user, translate("Picture"), _contact.party().picture(), null, pictureFieldSize));
		content.add(new LabeledPanel(translate("Nick"), new ReactiveTextField(_contact.nick(), nickChanger(), _font), _font));
		content.add(new LabeledPanel(translate("Name"), new ReactiveTextField(_contact.party().name(), null, _font), _font));
		content.add(new LabeledPanel(translate("Thought Of The Day"), new ReactiveTextField(_contact.party().thoughtOfTheDay(), null, _font), _font));
		content.add(new LabeledPanel(translate("Profile"), new ReactiveMemoField(_contact.party().profile(), null, _font,4),  _font));
		content.add(new LabeledPanel(translate("Host"), new ReactiveTextField(_contact.party().host(), null, _font),  _font));
		content.add(new LabeledPanel(translate("Port"), new ReactiveIntegerField(_contact.party().port(), null, _font),  _font));
		
		return content;
	}

	private Omnivore<String> nickChanger() {
		return new Omnivore<String>(){
			public void consume(String newNick) {
				try {
					_nickChanger.consume(new Pair<ContactId, String>(_contact.id(),newNick));
				} catch (IllegalParameter ignored) {
				}
			}
		};
	}
	
	private static final long serialVersionUID = 1L;
}
