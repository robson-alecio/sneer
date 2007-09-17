package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.Dimension;
import java.awt.Font;
import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import sneer.kernel.business.BusinessSource;
import wheel.io.ui.User;
import wheel.io.ui.widgets.ReactiveIntegerField;
import wheel.io.ui.widgets.ReactiveJpgImageField;
import wheel.io.ui.widgets.ReactiveMemoField;
import wheel.io.ui.widgets.ReactiveTextField;

public class LateralRootInfo extends JPanel{

	private final BusinessSource _businessSource;
	private final User _user;

	public LateralRootInfo( User user, BusinessSource businessSource){
		super();
		_user = user;
		_businessSource = businessSource;
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
		
		content.add(new ReactiveJpgImageField(_user, translate("Picture"), _businessSource.output().picture(), _businessSource.pictureSetter(), pictureFieldSize));
		content.add(new LabeledPanel(translate("Own Name"), new ReactiveTextField(_businessSource.output().ownName(), _businessSource.ownNameSetter(), fieldFont), defaultFieldSize, titleFont));
		content.add(new LabeledPanel(translate("Thought Of The Day"), new ReactiveTextField(_businessSource.output().thoughtOfTheDay(), _businessSource.thoughtOfTheDaySetter(), fieldFont), defaultFieldSize, titleFont));
		content.add(new LabeledPanel(translate("Profile"), new ReactiveMemoField(_businessSource.output().profile(), _businessSource.profileSetter(), fieldFont), profileFieldSize, titleFont));
		content.add(new LabeledPanel(translate("Sneer Port"), new ReactiveIntegerField(_businessSource.output().sneerPort(), _businessSource.sneerPortSetter(), fieldFont),defaultFieldSize, titleFont));

		return content;
	}
	
	private Font _defaultFont;
	
	public Font sneerFont(){
		if (_defaultFont == null){
			InputStream fis = LateralRootInfo.class.getResourceAsStream("/sneer/kernel/gui/contacts/bip.ttf");
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
