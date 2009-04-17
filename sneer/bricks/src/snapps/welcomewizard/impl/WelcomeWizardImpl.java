package snapps.welcomewizard.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import snapps.welcomewizard.WelcomeWizard;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.ByRef;
import sneer.hardware.gui.Action;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.port.PortKeeper;
import sneer.pulp.reactive.Signal;
import sneer.skin.main.menu.MainMenu;
import sneer.skin.widgets.reactive.NotificationPolicy;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import sneer.skin.windowboundssetter.WindowBoundsSetter;
import sneer.software.lang.PickyConsumer;
import wheel.reactive.impl.IntegerParser;

class WelcomeWizardImpl extends JFrame implements WelcomeWizard {
	
	private Environment _environment;
	private TextWidget<JTextField>  _yourOwnName;
	private TextWidget<JTextField> _sneerPort;
	
	private final JTextField _dynDnsHost = new JTextField();
	private final JTextField _dynDnsUser = new JTextField();
	private final JPasswordField _dynDnsPassword = new JPasswordField();
	
	private final OwnNameKeeper _nameKeeper = my(OwnNameKeeper.class);
	private final MainMenu _mainMenu = my(MainMenu.class);	
	
	WelcomeWizardImpl() {
		addWelcomeWindowAction();

		if(hasRequiredUserData()) return;
		
		_environment = my(Environment.class);
		initGui();
		restoreFieldData();
		open();
	}

	private void open() {
		if(isVisible()) return;
		
		my(WindowBoundsSetter.class).setBestBounds(this);
		setVisible(true);
	}
	
	private boolean hasRequiredUserData() {
		String ownName =  _nameKeeper.name().currentValue();
		if (ownName == null) return false;
		return !ownName.trim().isEmpty();
	}

	private void initDynDnsAccount(String dynDnsHost, String dynDnsUserName, String dynDnsPassword) {
		if (dynDnsUserName == null) return;
	
		my(DynDnsAccountKeeper.class).accountSetter().consume(
				new DynDnsAccount(dynDnsHost, dynDnsUserName, dynDnsPassword));
	}

	private void initGui() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("My Info");
		
		setSize(350, 260);
		
		java.awt.Container pnl = getContentPane();
		
		_yourOwnName = newTextField(_nameKeeper.name(), _nameKeeper.nameSetter());
		
		PortKeeper portKeeper = my(PortKeeper.class);
		_sneerPort = newTextField(portKeeper.port(), new IntegerParser(portKeeper.portSetter()));
		
		pnl.setLayout(new GridBagLayout());
		
		addWidget(_yourOwnName.getComponent(), "My Name:", 0);
		addWidget(_sneerPort.getComponent(), "Sneer Port:", 1);
		
		JPanel pnlDynDns = new JPanel();
		pnlDynDns.setLayout(new GridBagLayout());

		pnlDynDns.setBorder(new TitledBorder("My DynDns [Optional]"));
		getContentPane().add(pnlDynDns,
				new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0,
						GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),0, 0));
		
		addWidget(pnlDynDns, _dynDnsHost, "Host:", 0);
		addWidget(pnlDynDns, _dynDnsUser, "User:", 1);
		addWidget(pnlDynDns, _dynDnsPassword, "Password:", 2);
		
		final JButton btn = new JButton("OK");
		getContentPane().add(btn,
				new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
						GridBagConstraints.EAST,	GridBagConstraints.NONE, new Insets(0, 5, 5, 5),0, 0));

		btn.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent ignored) {
			submit();						
		}});
	}

	private void addWidget(JComponent widget, String label, int y) {	addWidget(getContentPane(), widget, label, y);	}
	private void addWidget(Container container, JComponent widget, String label, int y) {
		container.add(new JLabel(label),
				new GridBagConstraints(0, y, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST,	GridBagConstraints.NONE, new Insets(5, 5, 5, 5),0, 0));
		
		container.add(widget,
				new GridBagConstraints(1, y, 1, 1, 1.0, 0.0,
						GridBagConstraints.WEST,	GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),0, 0));
	}

	private TextWidget<JTextField> newTextField(final Signal<?> signal, final PickyConsumer<String> setter) {
		final ByRef<TextWidget<JTextField>> result = ByRef.newInstance();
		my(GuiThread.class).strictInvokeAndWait(new Runnable() { @Override public void run() {
			result.value = my(ReactiveWidgetFactory.class).newTextField(signal, setter, NotificationPolicy.OnEnterPressedOrLostFocus);
		}});
		return result.value;
	}

	private void submit() {
		Environments.runWith(_environment, new Runnable(){ @Override public void run() {
			try {
				storeFieldData();
				setVisible(false);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(WelcomeWizardImpl.this, ex.getMessage());
				ex.printStackTrace();
			}
		}});
	}
	
	private void restoreFieldData() {
		DynDnsAccount account = my(DynDnsAccountKeeper.class).ownAccount().currentValue();
		if (account == null) return;
		_dynDnsHost.setText(account.host);
		_dynDnsUser.setText(account.user);
		_dynDnsPassword.setText(account.password);
	}

	private void storeFieldData() throws Exception {
		initDynDnsAccount(trim(_dynDnsHost), trim(_dynDnsUser), trim(_dynDnsPassword));
	}

	private String trim(JTextField field) {
		return field.getText().trim();
	}
	
	private void addWelcomeWindowAction() {
		Action cmd = new Action(){
			@Override public String caption() { 	return "My Info...";	}
			@Override	public void run() { 				open(); }
		};
		_mainMenu.getSneerMenu().addAction(cmd);
	}
}