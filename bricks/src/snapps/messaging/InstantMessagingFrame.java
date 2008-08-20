package snapps.messaging;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

import org.apache.commons.lang.StringEscapeUtils;

import wheel.io.Log;
import wheel.io.ui.User.Notification;
import wheel.lang.FrozenTime;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class InstantMessagingFrame extends JFrame {
	
	private static final String TYPING = "TyPiNg :)"; //Refactor :)
	private static final String TYPING_PAUSED = "TyPiNg PaUsEd :)";
	private static final String TYPING_ERASED = "TyPiNg ErAsEd :)";

	public InstantMessagingFrame(Signal<String> otherGuysNick, final Signal<String> messageInput, Omnivore<String> messageOutput, Omnivore<Notification> briefUserNotifier){
		_otherGuysNick = otherGuysNick;
		_messageOutput = messageOutput;
		_briefUserNotifier = briefUserNotifier;
		
		initComponents();
		setVisible(true);
		
		_otherGuysNick.addReceiver(_nickReceiver);
		messageInput.addReceiver(_messageReceiver);
		
		startIsTypingNotifier();
		startChatDisplayer();
	}

	private final Omnivore<Notification> _briefUserNotifier;
	private final Signal<String> _otherGuysNick;
	private final Omnivore<String> _nickReceiver = createNickReceiver();
	private final Omnivore<String> _messageReceiver = createMessageReceiver();
	private final Omnivore<String> _messageOutput;

	private final JTextField _chatInput = createChatInput();
	private final JEditorPane _chatText = createChatText();
	private final List<String> _chatDisplayQueue = new LinkedList<String>();
	
	private final JLabel _statusLabel = new JLabel(" "); //Needs something to process preferred size and render correctly. :P
	private volatile long _lastMessageSendingTime;
	private volatile long _lastKeyPressedTime;
	private String _lastMessageDay;
	
	
	private void startChatDisplayer() {
		Threads.startDaemon("Chat Displayer", new Runnable() { public void run() { 
			while (true) {
				String text = waitForNextChatToDisplay();
				displayChat(text,true); 
			} 
		}}); 
		
	}
	
	private String waitForNextChatToDisplay() {
		synchronized (_chatDisplayQueue) {
			if (_chatDisplayQueue.isEmpty())
				Threads.waitWithoutInterruptions(_chatDisplayQueue);
			return _chatDisplayQueue.remove(0);
		}
	}

	private void queueChatForDisplay(String sender, String text, Date date) {
		String entry = messageDisplay(sender, text, date);
		queueChatForDisplay(entry);
	}
	

	private String messageDisplay(String sender, String text, Date date) {
		queueDaySeparatorIfNecessary();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String entry = formatter.format(date) + " <b>"+ sender + ":</b> " + text;  //FixUrgent: Use frozenDate()
		return entry;
	}

	private void queueDaySeparatorIfNecessary() {
		String today = new SimpleDateFormat("dd-MMM-yyyy").format(FrozenTime.frozenDate()); //FixUrgent: Use frozenDate()
		
		if (today.equals(_lastMessageDay)) return;

		try {
			if (_lastMessageDay == null) return;
			queueChatForDisplay("<b>" + today + "  --------------------------------" + "</b>");
		} finally {
			_lastMessageDay = today;
		}
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
		result.addKeyListener(keyTypedListener());
		return result;
	}


	private KeyListener keyTypedListener() {
		return new KeyAdapter() { @Override public void keyTyped(KeyEvent ignored) {
			_lastKeyPressedTime = System.currentTimeMillis();
		}};
	}

	private void startIsTypingNotifier() {
		Threads.startDaemon("Typing Notifier", new Runnable() { @Override public void run() {
			String previousStatus = TYPING_ERASED;
			while (true) {
				Threads.sleepWithoutInterruptions(500);

				if (_lastMessageSendingTime > _lastKeyPressedTime) {
					previousStatus = TYPING_ERASED;
					continue;
				}
				
				String text = _chatInput.getText();
				
				String status = text.isEmpty()
					? TYPING_ERASED
					: System.currentTimeMillis() - _lastKeyPressedTime > 4000
						? TYPING_PAUSED
						: TYPING;

				if (status == previousStatus) continue;
				previousStatus = status;
				
				_messageOutput.consume(status);
			}
		}});
	}

	private ActionListener chatActionListener() {
		return new ActionListener() { @Override public void actionPerformed(ActionEvent ignored) {
			final String message = StringEscapeUtils.escapeHtml(_chatInput.getText());
			_chatInput.setText("");
			
			_lastMessageSendingTime = System.currentTimeMillis();
			_messageOutput.consume(message);
			queueChatForDisplay(translate("Me"), message, FrozenTime.frozenDate());
		}};
	}

	private JEditorPane createChatText() {
		final JEditorPane chatArea = new JEditorPane();
		chatArea.setEditable(false);
		chatArea.setContentType("text/html");
		chatArea.setText("<html><div align = \"left\" id=\"textInsideThisDiv\"></div></html>");
		return chatArea;
	}
	
	private void queueChatForDisplay(String entry) {
		synchronized (_chatDisplayQueue) {
			_chatDisplayQueue.add(entry);
			_chatDisplayQueue.notify();
		}
	}

	private String processEmoticons(String text){ //Fix: free icons loaded from the internet... embed it... 
		text = text.replaceAll("\\:\\-\\)", "<img width=50 height=50 src = \"http://www.humorbabaca.com/emo/0072.gif\">"); // :-)
		text = text.replaceAll("\\:\\)", "<img width=50 height=50 src = \"http://www.humorbabaca.com/emo/0072.gif\">"); // :)
		text = text.replaceAll("\\:D", "<img width=50 height=50 src = \"http://www.humorbabaca.com/emo/0057.gif\">"); // :D
		text = text.replaceAll("\\;\\-\\)", "<img width=66 height=66 src = \"http://www.humorbabaca.com/emo/0076.gif\">"); // ;-)
		text = text.replaceAll("\\;\\)", "<img width=66 height=66 src = \"http://www.humorbabaca.com/emo/0076.gif\">"); // ;)
		text = text.replaceAll("\\:\\-\\(", "<img width=61 height=50 src = \"http://www.humorbabaca.com/emo/0075.gif\">"); // :-(
		text = text.replaceAll("\\:\\(", "<img width=61 height=50 src = \"http://www.humorbabaca.com/emo/0075.gif\">"); // :(
		text = text.replaceAll("\\:\\-P", "<img width=50 height=50 src = \"http://www.humorbabaca.com/emo/0004.gif\">"); // :-P
		text = text.replaceAll("\\:P", "<img width=50 height=50 src = \"http://www.humorbabaca.com/emo/0004.gif\">"); // :P
		text = text.replaceAll("rara", "<img width=60 height=44 src = \"http://www.humorbabaca.com/emo/0037.gif\">"); // rara
		text = text.replaceAll("ecati", "<img width=50 height=48 src = \"http://www.humorbabaca.com/emo/0006.gif\">"); // ecati
		text = text.replaceAll("Peccin", "<img width=61 height=50 src = \"http://www.humorbabaca.com/emo/0075.gif\">"); // Peccin
		text = text.replaceAll("peccin", "<img width=61 height=50 src = \"http://www.humorbabaca.com/emo/0075.gif\">"); // peccin
		return text;
		
		//Fix: <) produces the same emoticon as ;)  . This should not happen.
	}
	
	private void receiveMessage(String message) {
		if (typingMessageHandled(message)) return;
		showNotificationIfNecessary(message);
		queueChatForDisplay(nick(), message, new Date());
	}

	private void showNotificationIfNecessary(String message) {
		if (message.length() == 0) return;
		if ((getExtendedState()&Frame.ICONIFIED) != 1) return;

		_briefUserNotifier.consume(new Notification(_otherGuysNick.currentValue(), message)); //Fix: Seems not to be working.
	}

	private String nick() {
		return _otherGuysNick.currentValue();
	}

	private boolean typingMessageHandled(String message) {
		if (message.equals(TYPING)) {
			showStatus(translate(" %1$s is typing...", nick()));
			return true;
		}
		if (message.equals(TYPING_PAUSED)) {
			showStatus(translate(" %1$s has entered text...", nick()));
			return true;
		}
		showStatus(" ");
		if (message.equals(TYPING_ERASED)) return true;
		
		return false;
	}

	private void showStatus(final String status) {
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() {
			_statusLabel.setText(status);
		}});
	}

	private void displayChatHtml(String chatHtml, boolean delay) {
		HTMLDocument document = (HTMLDocument)_chatText.getDocument();
		Element ep = document.getElement("textInsideThisDiv");
		try {
			if (delay)
				Threads.sleepWithoutInterruptions(300); //Refactor: Remove this line in the future and send a stream of messages to see if Swing still hangs (Klaus. July 2007 (jre1.6.0_01))
			document.insertBeforeEnd(ep, chatHtml);
		} catch (Exception ex) {
			Log.log(ex);
		}
		_chatText.setCaretPosition(document.getLength());
	}

	private void displayChat(String text, final boolean delay) {
		final String html = buildHtmlLine(text);
		try {
			SwingUtilities.invokeAndWait(new Runnable() { @Override public void run() {
				displayChatHtml(html,delay);
				//Refactor Consider using invokeLater, with the catch blocks inside this runnable, so that we dont need the _chatDisplayQueue.
			}});
		} catch (RuntimeException e) {
			Log.log(e);
		} catch (InterruptedException e) {
			Log.log(e);
		} catch (InvocationTargetException e) {
			Log.log(e);
		}
	}

	private String buildHtmlLine(String text) {
		return "<div><font face=\"Verdana\" size=\"3\">" + processEmoticons(text) + "</font></div>";
	}

	private Omnivore<String> createMessageReceiver() {
		return new Omnivore<String>() { @Override public void consume(final String message) {
			receiveMessage(message);
		}};
	}

	private Omnivore<String> createNickReceiver() {
		return new Omnivore<String>() { @Override public void consume(String nick) {
			setTitle(nick + " - Instant Messaging");
		}};
	}

	private static final long serialVersionUID = 1L;

}
