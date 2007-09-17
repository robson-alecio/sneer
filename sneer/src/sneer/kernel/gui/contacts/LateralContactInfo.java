package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.Dimension;
import java.awt.Font;
import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.widgets.ReactiveIntegerField;
import wheel.io.ui.widgets.ReactiveJpgImageField;
import wheel.io.ui.widgets.ReactiveMemoField;
import wheel.io.ui.widgets.ReactiveTextField;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;

public class LateralContactInfo extends JPanel{

	private final Consumer<Pair<ContactId, String>> _nickChanger;
	private final Contact _contact;

	public LateralContactInfo( Contact contact, Consumer<Pair<ContactId, String>> nickChanger){
		super();
		_contact = contact;
		_nickChanger = nickChanger;
		add(contentPanel());
	}
	
	private JPanel contentPanel(){
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		
		Dimension defaultFieldSize = new Dimension(150,45);
		Dimension profileFieldSize = new Dimension(150,100);
		Dimension pictureFieldSize = new Dimension(150,150);
		
		Font fieldFont = sneerFont(12);
		Font titleFont = sneerFont(14);
		
		content.add(new ReactiveJpgImageField(translate("Picture"), _contact.party().picture(), null, pictureFieldSize),titleFont);
		content.add(new LabeledPanel(translate("Nick"), new ReactiveTextField(_contact.party().name(), nickChanger(), fieldFont), defaultFieldSize,titleFont));
		content.add(new LabeledPanel(translate("Thought Of The Day"), new ReactiveTextField(_contact.party().thoughtOfTheDay(), null, fieldFont), defaultFieldSize, titleFont));
		content.add(new LabeledPanel(translate("Profile"), new ReactiveMemoField(_contact.party().profile(), null, fieldFont), profileFieldSize, titleFont));
		content.add(new LabeledPanel(translate("Host"), new ReactiveTextField(_contact.party().host(), null, fieldFont), defaultFieldSize, titleFont));
		content.add(new LabeledPanel(translate("Port"), new ReactiveIntegerField(_contact.party().port(), null, fieldFont), defaultFieldSize, titleFont));
		
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

	private Font _defaultFont;
	
	public Font sneerFont(){
		if (_defaultFont == null){
			InputStream fis = LateralContactInfo.class.getResourceAsStream("/sneer/kernel/gui/contacts/bip.ttf");
			try{
				_defaultFont =  Font.createFont(Font.TRUETYPE_FONT, fis);
			}catch(Exception e){
				_defaultFont = new Font("Arial",Font.PLAIN,14);
			}
		}
		return _defaultFont;
	}
	
	public Font sneerFont(float size){
		return sneerFont().deriveFont(size);
	}
	
	private static final long serialVersionUID = 1L;
}
