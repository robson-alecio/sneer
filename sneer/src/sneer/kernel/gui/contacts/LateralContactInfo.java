package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.pointofview.Contact;
import wheel.lang.Consumer;
import wheel.lang.Pair;

public class LateralContactInfo extends JPanel{

	@SuppressWarnings("unused") //Should be used in nick changing...
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
		
		content.add(new ReactiveJpgImageField(translate("Picture"), _contact.party().picture(), null, pictureFieldSize));
		content.add(new LabeledPanel(translate("Nick"), new ReactiveTextField(_contact.party().name(), null), defaultFieldSize)); //Fix: the user should be able to change the nick here!
		content.add(new LabeledPanel(translate("Thought Of The Day"), new ReactiveTextField(_contact.party().thoughtOfTheDay(), null), defaultFieldSize));
		content.add(new LabeledPanel(translate("Profile"), new ReactiveMemoField(_contact.party().profile(), null), profileFieldSize));
		content.add(new LabeledPanel(translate("Host"), new ReactiveTextField(_contact.party().host(), null), defaultFieldSize));
		content.add(new LabeledPanel(translate("Port"), new ReactiveIntegerField(_contact.party().port(), null), defaultFieldSize));
		
		return content;
	}
	
	private static final long serialVersionUID = 1L;
}
