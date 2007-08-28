package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.pointofview.Contact;
import wheel.graphics.JpgImage;
import wheel.lang.Consumer;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;

public class LateralInfo extends JPanel{

	final static String IMAGE_PATH = "/sneer/kernel/gui/contacts/images/";

	final static ImageIcon NO_IMAGE = new ImageIcon(LateralInfo.class.getResource(IMAGE_PATH + "questionmark.jpg"));

	
	private JButton _updateButton;
	private final Consumer<Pair<ContactId, String>> _nickChanger;
	private InfoTextField _nickField;
	private final Contact _contact;

	public LateralInfo( Contact contact, Consumer<Pair<ContactId, String>> nickChanger){
		super();
		_contact = contact;
		_nickChanger = nickChanger;
		add(contentPanel());
	}
	
	private JPanel contentPanel(){
		setSize(new Dimension(120,100));
		JpgImage picture = _contact.party().picture().currentValue();
		ImageIcon pictureIcon = NO_IMAGE;
		if (picture != null)
			pictureIcon = new ImageIcon(picture.contents());
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		JLabel pictureLabel =new JLabel();
		pictureLabel.setIcon(pictureIcon);
		pictureLabel.setSize(100,100);
		pictureLabel.setBorder(new LineBorder(Color.black));
		content.add(pictureLabel);
		_nickField = new InfoTextField(translate("Nick"),_contact.nick().currentValue(),true);
		content.add(_nickField);
		content.add(new InfoTextField(translate("Host"),_contact.party().host().currentValue(),false));
		content.add(new InfoTextField(translate("Port"),""+_contact.party().port().currentValue(),false));
		content.add(new InfoTextField(translate("Thought Of The Day"),_contact.party().thoughtOfTheDay().currentValue(),false));
		content.add(new InfoTextField(translate("Profile"),_contact.party().profile().currentValue(),false));
		
		_updateButton = new JButton(translate("Update"));
		_updateButton.addActionListener(changeNickAction());
		content.add(_updateButton);
		return content;
	}

	private ActionListener changeNickAction() {
		return new ActionListener(){ public void actionPerformed(ActionEvent e) {
				try {
					_nickChanger.consume(new Pair<ContactId, String>(_contact.id(),_nickField.getText()));
				} catch (IllegalParameter ignored) {
					
				}
		}};
	}
	
	public class InfoTextField extends JPanel{

		private JTextField _area = new JTextField();
		private JLabel _label = new JLabel();

		public InfoTextField(String fieldName, String defaultValue, boolean editable){
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			_label.setText(fieldName);
			_area.setText(defaultValue);
			_area.selectAll();
			_area.setEditable(editable);
			add(_label);
			add(_area);
		}
		
		public String getText(){
			return _area.getText();
		}
		
		private static final long serialVersionUID = 1L;
		
	}
	
	private static final long serialVersionUID = 1L;
}
