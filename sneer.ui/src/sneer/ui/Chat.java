package sneer.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public abstract class Chat extends Composite {

	private Text _correspondence;
	private Text _draft;
	private final String _contact;

	public Chat(Composite parent, int style, String contact) {
		super(parent, style);
		_contact = contact;
		
		initialize();
		
	}

	private void initialize() {
		setSize(400, 300);
		
		setLayout(new GridLayout());
		
		GridData gd;
		_correspondence = new Text(this, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		_correspondence.setLayoutData(gd);
		
		_draft = new Text(this, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.heightHint = 60;
		_draft.setLayoutData(gd);
		_draft.addKeyListener(new KeyAdapter() {

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
		String msg = _draft.getText();
		_draft.setText("");
		sendMessage(msg);
		addTextToCorrespondence("You: " + msg);
	}

	private void addTextToCorrespondence(String msg) {
		_correspondence.setText(_correspondence.getText()+ msg+"\r\n");
		_correspondence.setSelection(_correspondence.getText().length()-1);
	}
	
	public void addCorrespondence(String received) {
		addTextToCorrespondence(_contact + ": " + received);
	}
	
	protected abstract void sendMessage(String text);
	

}
