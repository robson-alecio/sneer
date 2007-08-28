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

import sneer.kernel.business.BusinessSource;
import wheel.graphics.JpgImage;

public class LateralRootInfo extends JPanel{

	final static String IMAGE_PATH = "/sneer/kernel/gui/contacts/images/";
	final static ImageIcon NO_IMAGE = new ImageIcon(LateralRootInfo.class.getResource(IMAGE_PATH + "questionmark.jpg"));

	private JButton _updateButton;
	
	private InfoTextField _ownNameField;
	private InfoTextField _sneerPortField;
	private InfoTextField _thoughtOfTheDayField;
	private InfoTextField _profileField;
	
	private final BusinessSource _businessSource;

	public LateralRootInfo( BusinessSource businessSource){
		super();
		_businessSource = businessSource;
		add(contentPanel());
	}
	
	private JPanel contentPanel(){
		setSize(new Dimension(120,100));
		JpgImage picture = _businessSource.output().picture().currentValue();
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
		_ownNameField = new InfoTextField(translate("Own Name"),_businessSource.output().ownName().currentValue(),true);
		_sneerPortField = new InfoTextField(translate("Sneer Port"),""+_businessSource.output().sneerPort().currentValue(),true);
		_thoughtOfTheDayField = new InfoTextField(translate("Thought Of The Day"),_businessSource.output().thoughtOfTheDay().currentValue(),true);
		_profileField = new InfoTextField(translate("Profile"),_businessSource.output().profile().currentValue(),true);
		content.add(_ownNameField);
		content.add(_sneerPortField);
		content.add(_thoughtOfTheDayField);
		content.add(_profileField);
		
		_updateButton = new JButton(translate("Update"));
		_updateButton.addActionListener(ownNameChangeAction());
		_updateButton.addActionListener(sneerPortChangeAction());
		_updateButton.addActionListener(thoughtOfTheDayChangeAction());
		_updateButton.addActionListener(profileChangeAction());
		content.add(_updateButton);
		return content;
	}

	private ActionListener ownNameChangeAction() {
		return new ActionListener(){ public void actionPerformed(ActionEvent e) {
				if (!_businessSource.output().ownName().currentValue().equals(_ownNameField.getText()))
					_businessSource.ownNameSetter().consume(_ownNameField.getText());
		}};
	}
	
	private ActionListener sneerPortChangeAction() {
		return new ActionListener(){ public void actionPerformed(ActionEvent e) {
				try {
					if (!_businessSource.output().sneerPort().currentValue().equals(new Integer(_sneerPortField.getText())))
					_businessSource.sneerPortSetter().consume(new Integer(_sneerPortField.getText()));
				} catch (Exception ignored) {
					
				}
		}};
	}
	
	private ActionListener thoughtOfTheDayChangeAction() {
		return new ActionListener(){ public void actionPerformed(ActionEvent e) {
			if (!_businessSource.output().thoughtOfTheDay().currentValue().equals(_thoughtOfTheDayField.getText()))
				_businessSource.thoughtOfTheDaySetter().consume(_thoughtOfTheDayField.getText());
		}};
	}
	private ActionListener profileChangeAction() {
		return new ActionListener(){ public void actionPerformed(ActionEvent e) {
			if (!_businessSource.output().profile().currentValue().equals(_profileField.getText()))
				_businessSource.profileSetter().consume(_profileField.getText());

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
