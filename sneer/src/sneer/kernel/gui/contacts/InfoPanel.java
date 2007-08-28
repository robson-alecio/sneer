package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.pointofview.Contact;
import wheel.lang.Consumer;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;

public class InfoPanel{

	private final Contact _contact;
	private JButton _updateButton;
	private final Consumer<Pair<ContactId, String>> _nickChanger;
	private InfoTextField _nickField;

	public InfoPanel(Contact contact, Consumer<Pair<ContactId, String>> nickChanger){
		_contact = contact;
		_nickChanger = nickChanger;

		JFrame frame = new JFrame();
		frame.getContentPane().add(contentPanel());
		frame.pack();
		frame.setVisible(true);
	}
	
	private JPanel contentPanel(){
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		_nickField = new InfoTextField(translate("Nick"),_contact.nick().currentValue(),true);
		content.add(_nickField);
		content.add(new InfoTextField(translate("Host"),_contact.party().host().currentValue(),false));
		content.add(new InfoTextField(translate("Port"),""+_contact.party().port().currentValue(),false));
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

		private JTextField _field = new JTextField();
		private JLabel _label = new JLabel();

		public InfoTextField(String fieldName, String defaultValue, boolean editable){
			setLayout(new BorderLayout());
			_field.setPreferredSize(new Dimension(100,20));
			_label.setPreferredSize(new Dimension(50,20));
			_label.setText(fieldName);
			_field.setText(defaultValue);
			_field.selectAll();
			_field.setEditable(editable);
			add(_label,BorderLayout.WEST);
			add(_field,BorderLayout.CENTER);
		}
		
		public String getText(){
			return _field.getText();
		}
		
		private static final long serialVersionUID = 1L;
		
	}
	
}
