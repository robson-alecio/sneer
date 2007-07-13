package sneer.apps.conversations.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

import sneer.apps.conversations.Message;
import wheel.io.Log;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class ConversationFrame extends JFrame {
	
	private static final String TYPING = "TyPiNg :)";
	private static final String TYPING_PAUSED = "TyPiNg PaUsEd :)";
	private static final String TYPING_ERASED = "TyPiNg ErAsEd :)";


	public ConversationFrame(Signal<String> otherGuysNick, final Signal<Message> messageInput, Omnivore<Message> messageOutput){
		_otherGuysNick = otherGuysNick;
		_messageOutput = messageOutput;
		
		initComponents();
		
		_otherGuysNick.addReceiver(new Omnivore<String>() { @Override public void consume(String nick) {
			setTitle(nick);
		}});
		
		messageInput.addReceiver(new Omnivore<Message>() { @Override public void consume(final Message message) {
			receiveMessage(message);
		}});
		
		setVisible(true);
	}

	private final Signal<String> _otherGuysNick;
	private final Omnivore<Message> _messageOutput;

	private final JEditorPane _chatText = createChatText();
	private final JTextField _chatInput = createChatInput();
	private final JLabel _statusLabel = new JLabel(" "); //Needs something to process preferred size and render correctly. :P

	private volatile Runnable _lastTypingHandler;
	
	private void appendToChatText(String sender, Message message) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		appendToChatText(formatter.format(new Date()) + " <b>"+ sender + ":</b> " + message._text);
	}
	
	private void initComponents() {
		setLayout(new BorderLayout());

		add(new JScrollPane(_chatText), BorderLayout.CENTER);
		add(createInputPanel(), BorderLayout.SOUTH);
		
		setSize(300, 200);
	}

	private JPanel createInputPanel() {
		JPanel result = new JPanel(new BorderLayout());
		result.add(_chatInput, BorderLayout.CENTER);
		result.add(_statusLabel, BorderLayout.SOUTH);
		return result;
	}

	private JTextField createChatInput() {
		JTextField result = new JTextField();
		result.addActionListener(chatActionListener());
		result.addKeyListener(chatKeyListener());
		return result;
	}

	private KeyListener chatKeyListener() { //Fix: All this typing logic is not working 100%
		return new KeyAdapter() {	@Override	public void keyTyped(KeyEvent ignored) {
			
			final Runnable previousTypingHandler = _lastTypingHandler;
		
			Threads.startDaemon(new Runnable() {
				public void run() {
					_lastTypingHandler = this;
					if (previousTypingHandler == null) _messageOutput.consume(new Message(TYPING));
		
					Threads.sleepWithoutInterruptions(4000);
		
					if (_lastTypingHandler != this) return;
					typingDone(false);
				}
			}
		);}};
	}

	private void typingDone(boolean messageSent) {
		_lastTypingHandler = null;
		if (messageSent) return;
		
		String text = _chatInput.getText().isEmpty()
			? TYPING_ERASED
			: TYPING_PAUSED;
		_messageOutput.consume(new Message(text));
	}
	
	private ActionListener chatActionListener() {
		return new ActionListener() { @Override public void actionPerformed(ActionEvent ignored) {
			final Message message = new Message(_chatInput.getText());
			_chatInput.setText("");
				
			Threads.startDaemon(new Runnable() { @Override public void run() {
				_messageOutput.consume(message);
				appendToChatText(translate("Me"), message);
				typingDone(true);
			}});
		}};
	}

	private JEditorPane createChatText() {
		final JEditorPane chatArea = new JEditorPane();
		chatArea.setEditable(false);
		chatArea.setContentType("text/html");
		chatArea.setText("<html><div align = \"left\" id=\"textInsideThisDiv\"></div></html>");
		return chatArea;
	}
	
	private void appendToChatText(final String entry) {
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() {
			HTMLDocument document = (HTMLDocument)_chatText.getDocument();
			Element ep = document.getElement("textInsideThisDiv");
			try {
				document.insertBeforeEnd(ep, "<div><font face=\"Verdana\" size=\"3\">" + processEmoticons(entry) + "</font></div>");
			} catch (Exception ex) {
				Log.log(ex);
			}
			_chatText.setCaretPosition(document.getLength());
		}});		
	}

	private String processEmoticons(String text){ //Fix: free icons loaded from the internet... embed it... 
		text = text.replaceAll("\\:\\-\\)", "<img src = \"http://www.humorbabaca.com/emo/0072.gif\">"); // :-)
		text = text.replaceAll("\\:\\)", "<img src = \"http://www.humorbabaca.com/emo/0072.gif\">"); // :)
		text = text.replaceAll("\\:D", "<img src = \"http://www.humorbabaca.com/emo/0057.gif\">"); // :D
		text = text.replaceAll("\\;\\-\\)", "<img src = \"http://www.humorbabaca.com/emo/0076.gif\">"); // ;-)
		text = text.replaceAll("\\;\\)", "<img src = \"http://www.humorbabaca.com/emo/0076.gif\">"); // ;)
		text = text.replaceAll("\\:\\-\\(", "<img src = \"http://www.humorbabaca.com/emo/0075.gif\">"); // :-(
		text = text.replaceAll("\\:\\(", "<img src = \"http://www.humorbabaca.com/emo/0075.gif\">"); // :(
		text = text.replaceAll("\\:\\-P", "<img src = \"http://www.humorbabaca.com/emo/0004.gif\">"); // :-P
		text = text.replaceAll("\\:P", "<img src = \"http://www.humorbabaca.com/emo/0004.gif\">"); // :P
		text = text.replaceAll("rara", "<img src = \"http://www.humorbabaca.com/emo/0037.gif\">"); // rara
		text = text.replaceAll("ecati", "<img src = \"http://www.humorbabaca.com/emo/0006.gif\">"); // ecati
		text = text.replaceAll("Peccin", "<img src = \"http://www.humorbabaca.com/emo/0075.gif\">"); // Peccin
		text = text.replaceAll("peccin", "<img src = \"http://www.humorbabaca.com/emo/0075.gif\">"); // peccin
		return text;
	}
	
	private void receiveMessage(Message message) {
		if (typingMessageHandled(message)) return;
		appendToChatText(nick(), message);
	}

	private String nick() {
		return _otherGuysNick.currentValue();
	}

	private boolean typingMessageHandled(Message message) {
		if (message._text.equals(TYPING)) {
			showStatus(translate(" %1$s is typing...", nick()));
			return true;
		}
		if (message._text.equals(TYPING_PAUSED)) {
			showStatus(translate(" %1$s has entered text...", nick()));
			return true;
		}
		showStatus(" ");
		if (message._text.equals(TYPING_ERASED)) return true;
		
		return false;
	}

	private void showStatus(final String status) {
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() {
			_statusLabel.setText(status);
		}});
	}

	private static final long serialVersionUID = 1L;
}
