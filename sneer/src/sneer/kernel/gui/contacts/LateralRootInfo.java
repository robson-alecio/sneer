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
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));

		Dimension defaultFieldSize = new Dimension(150,40);
		
		content.add(new ReactiveJpgImageField(translate("Picture"), _businessSource.output().picture(), _businessSource.pictureSetter(), new Dimension(100,100)));
		content.add(new LabeledPanel(translate("Own Name"), new ReactiveTextField(_businessSource.output().ownName(), _businessSource.thoughtOfTheDaySetter()),defaultFieldSize));
		content.add(new LabeledPanel(translate("Thought Of The Day"), new ReactiveTextField(_businessSource.output().thoughtOfTheDay(), _businessSource.thoughtOfTheDaySetter()),defaultFieldSize));
		content.add(new LabeledPanel(translate("Profile"), new ReactiveTextField(_businessSource.output().profile(), _businessSource.profileSetter()),defaultFieldSize));
		content.add(new LabeledPanel(translate("Sneer Port"), new ReactiveIntegerField(_businessSource.output().sneerPort(), _businessSource.sneerPortSetter()),defaultFieldSize));

		return content;
	}
	
	private static final long serialVersionUID = 1L;
}
