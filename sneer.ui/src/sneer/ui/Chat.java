package sneer.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public abstract class Chat extends Composite {

	private Text correspondence;
	private Text draft;

	public Chat(Composite parent, int style) {
		super(parent, style);
		
		initialize();
		
	}

	private void initialize() {
		setSize(400, 300);
		
		setLayout(new GridLayout());
		
		GridData gd;
		correspondence = new Text(this, SWT.MULTI | SWT.READ_ONLY);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		correspondence.setLayoutData(gd);
		
		draft = new Text(this, SWT.SINGLE);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		draft.setLayoutData(gd);
		draft.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					sendDraft();
				}
			}
			
		});

	}

	protected void sendDraft() {
		String msg = draft.getText();
		draft.setText("");
		sendMessage(msg);
		addTextToCorrespondence("sent: " + msg);
	}

	private void addTextToCorrespondence(String msg) {
		String before = correspondence.getText();
		if (before.length() > 0) {
			before += "\n";
		}
		correspondence.setText(before+ msg);
	}
	
	public void addCorrespondence(String received) {
		addTextToCorrespondence("received: "+received);
	}
	
	protected abstract void sendMessage(String text);
	

}
