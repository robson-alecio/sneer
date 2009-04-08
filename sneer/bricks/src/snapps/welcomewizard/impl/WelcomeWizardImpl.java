package snapps.welcomewizard.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import snapps.welcomewizard.WelcomeWizard;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.ByRef;
import sneer.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.port.PortKeeper;
import sneer.pulp.reactive.Signal;
import sneer.skin.main_Menu.MainMenu;
import sneer.skin.widgets.reactive.NotificationPolicy;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.GuiThread;
import wheel.io.ui.action.Action;
import wheel.lang.PickyConsumer;
import wheel.reactive.impl.IntegerParser;

class WelcomeWizardImpl extends JFrame implements WelcomeWizard {
	
	private Environment _environment;
	private TextWidget<JTextField>  _yourOwnName;
	private TextWidget<JTextField> _sneerPort;
	
	private final JTextField _dynDnsHost = new JTextField();
	private final JTextField _dynDnsUser = new JTextField();
	private final JTextField _dnyDnsPassword = new JTextField();
	
	private final OwnNameKeeper _nameKeeper = my(OwnNameKeeper.class);
	private final MainMenu _mainMenu = my(MainMenu.class);	
	
	WelcomeWizardImpl() {
		addWelcomeWindowAction();

		if(hasRequiredUserData()) return;
		
		_environment = my(Environment.class);
		initGui();
		restoreFieldData();
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
		setTitle("User Info");
		setBounds(10, 10, 350, 300);

		java.awt.Container pnl = getContentPane();
		
		_yourOwnName = newTextField(_nameKeeper.name(), _nameKeeper.nameSetter());
		
		PortKeeper portKeeper = my(PortKeeper.class);
		_sneerPort = newTextField(portKeeper.port(), new IntegerParser(portKeeper.portSetter()));
		
		pnl.setLayout(new GridLayout(6,1));
		pnl.add(_yourOwnName.getComponent());
		pnl.add(_sneerPort.getComponent());
		pnl.add(_dynDnsHost);
		pnl.add(_dynDnsUser);
		pnl.add(_dnyDnsPassword);
		
		final JButton btn = new JButton("OK");
		btn.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent ignored) {
			submit();						
		}});
		pnl.add(btn);

		label(_yourOwnName.getComponent(), "Own Name");
		label(_sneerPort.getComponent(), "Sneer Port");
		label(_dynDnsHost, "DynDns Host [optional]");
		label(_dynDnsUser, "DynDns User [optional]");
		label(_dnyDnsPassword, "DynDns Password [optional]");
	}

	private TextWidget<JTextField> newTextField(final Signal<?> signal, final PickyConsumer<String> setter) {
		final ByRef<TextWidget<JTextField>> result = ByRef.newInstance();
		GuiThread.strictInvokeAndWait(new Runnable() { @Override public void run() {
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
		_dnyDnsPassword.setText(account.password);
	}

	private void storeFieldData() throws Exception {
		initDynDnsAccount(trim(_dynDnsHost), trim(_dynDnsUser), trim(_dnyDnsPassword));
	}

	private void label(JComponent field, String caption) {
		field.setBorder(new TitledBorder(caption));
	}

	private String trim(JTextField field) {
		return field.getText().trim();
	}
	
	private void addWelcomeWindowAction() {
		Action cmd = new Action(){
			@Override
			public String caption() {
				return "User Info...";
			}
			@Override
			public void run() {
				WelcomeWizardImpl.this.setVisible(true);
			}
		};
		_mainMenu.getSneerMenu().addAction(cmd);
	}
}