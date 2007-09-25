package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import sneer.kernel.business.BusinessSource;
import wheel.io.ui.User;
import wheel.io.ui.widgets.ReactiveIntegerField;
import wheel.io.ui.widgets.ReactiveJpgImageField;
import wheel.io.ui.widgets.ReactiveMemoField;
import wheel.io.ui.widgets.ReactiveTextField;
import wheel.reactive.Signal;

public class LateralRootInfo extends JPanel{

	private final BusinessSource _businessSource;
	private final User _user;

	public LateralRootInfo( User user, BusinessSource businessSource){
		setLayout(new BorderLayout());
		_user = user;
		_businessSource = businessSource;
		add(contentPanel(),BorderLayout.NORTH);
		add(new JPanel(),BorderLayout.CENTER);
	}
	
	private JPanel contentPanel(){
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));

		Dimension pictureFieldSize = new Dimension(100,100);
		Signal<Font> font = _businessSource.output().font();
		content.add(new ReactiveJpgImageField(_user, translate("Picture"), _businessSource.output().picture(), _businessSource.pictureSetter(), pictureFieldSize));
		content.add(new LabeledPanel(translate("Own Name"), new ReactiveTextField(_businessSource.output().ownName(), _businessSource.ownNameSetter(), font), font));
		content.add(new LabeledPanel(translate("Thought Of The Day"), new ReactiveTextField(_businessSource.output().thoughtOfTheDay(), _businessSource.thoughtOfTheDaySetter(), font), font));
		content.add(new LabeledPanel(translate("Profile"), new ReactiveMemoField(_businessSource.output().profile(), _businessSource.profileSetter(), font,4), font));
		content.add(new LabeledPanel(translate("Sneer Port"), new ReactiveIntegerField(_businessSource.output().sneerPort(), _businessSource.sneerPortSetter(), font), font));

		return content;
	}
	
	private static final long serialVersionUID = 1L;
}
