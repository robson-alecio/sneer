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
		correspondence = new Text(this, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		correspondence.setLayoutData(gd);
		
		draft = new Text(this, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.heightHint = 60;
		draft.setLayoutData(gd);
		draft.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					sendDraft();
					e.keyCode = 0;
					e.doit = false;
					e.character = 0;
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
		correspondence.setText(correspondence.getText()+ msg+"\n");
		correspondence.setSelection(correspondence.getText().length()-1);
	}
	
	public void addCorrespondence(String received) {
		addTextToCorrespondence("received: "+received);
	}
	
	protected abstract void sendMessage(String text);
	

}
