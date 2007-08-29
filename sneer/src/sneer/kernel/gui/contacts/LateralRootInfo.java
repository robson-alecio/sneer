package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import sneer.kernel.business.BusinessSource;

public class LateralRootInfo extends JPanel{

	private final BusinessSource _businessSource;

	public LateralRootInfo( BusinessSource businessSource){
		super();
		_businessSource = businessSource;
		add(contentPanel());
	}
	
	private JPanel contentPanel(){
		setSize(new Dimension(120,100));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));

		content.add(new ReactiveJpgImageField("Picture", _businessSource.output().picture(), _businessSource.pictureSetter(), new Dimension(100,100)));
		content.add(new ReactiveTextField(translate("Own Name"), _businessSource.output().ownName(), _businessSource.thoughtOfTheDaySetter()));
		content.add(new ReactiveTextField(translate("Thought Of The Day"), _businessSource.output().thoughtOfTheDay(), _businessSource.thoughtOfTheDaySetter()));
		content.add(new ReactiveTextField(translate("Profile"), _businessSource.output().profile(), _businessSource.profileSetter()));
		content.add(new ReactiveIntegerField(translate("Sneer Port"), _businessSource.output().sneerPort(), _businessSource.sneerPortSetter()));

		return content;
	}
	
	private static final long serialVersionUID = 1L;
}
