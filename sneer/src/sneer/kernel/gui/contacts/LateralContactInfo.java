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
		setSize(new Dimension(120,100));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		
		content.add(new ReactiveJpgImageField("Picture",_contact.party().picture(),null,new Dimension(100,100)));
		content.add(new ReactiveTextField(translate("Nick"),_contact.party().name(),null)); //Fix: the user should be able to change the nick here!
		content.add(new ReactiveTextField(translate("Host"),_contact.party().host(),null));
		content.add(new ReactiveTextField(translate("Thought Of The Day"),_contact.party().thoughtOfTheDay(),null));
		content.add(new ReactiveTextField(translate("Profile"),_contact.party().profile(),null));
		content.add(new ReactiveIntegerField(translate("Port"),_contact.party().port(),null));
		
		return content;
	}
	
	private static final long serialVersionUID = 1L;
}
