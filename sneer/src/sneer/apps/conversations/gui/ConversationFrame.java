package sneer.apps.conversations.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import sneer.apps.conversations.Message;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class ConversationFrame extends JFrame {
	
	private static final String TYPING = "TyPiNg :)";


	public ConversationFrame(Signal<String> otherGuysNick, final Signal<Message> messageInput, Omnivore<Message> messageOutput){
		_otherGuysNick = otherGuysNick;
		_messageOutput = messageOutput;
		
		initComponents();
		
		_otherGuysNick.addReceiver(new Omnivore<String>() { @Override public void consume(String nick) {
			setTitle(nick);
		}});
		
		messageInput.addReceiver(new Omnivore<Message>() { @Override public void consume(Message message) {
			String nick = _otherGuysNick.currentValue();
			
			if (message._text.equals(TYPING)) { //Refactor :)
				_statusLabel.setText(translate(" %1$s is typing... :)",nick));
				return;
			}
			_statusLabel.setText(" ");
			
			appendToChatText(nick, message);
		}});
		
		setVisible(true);
	}

	private final Signal<String> _otherGuysNick;
	private final Omnivore<Message> _messageOutput;

	private final JTextArea _chatText = createChatText();
	private final JLabel _statusLabel = new JLabel(" ");
	private boolean _isTyping = false;

	
	private void appendToChatText(String sender, Message message) {
		appendToChatText(sender + ": " + message._text);
	}
	
	private void initComponents() {
		setLayout(new BorderLayout());
		_chatText.setLineWrap(true);
		add(new JScrollPane(_chatText), BorderLayout.CENTER);
		add(createInputPanel(), BorderLayout.SOUTH);
		
		setSize(300, 200);
	}

	private JPanel createInputPanel() {
		JPanel result = new JPanel(new BorderLayout());
		result.add(createChatInput(), BorderLayout.CENTER);
		result.add(_statusLabel, BorderLayout.SOUTH);
		return result;
	}

	private JTextField createChatInput() {
		final JTextField chatInput = new JTextField();
		chatInput.addActionListener(chatActionListener(chatInput));
		chatInput.addKeyListener(chatKeyListener());
		return chatInput;
	}

	private KeyListener chatKeyListener() {
		return new KeyAdapter() {	@Override	public void keyTyped(KeyEvent ignored) {
			if (!_isTyping) _messageOutput.consume(new Message(TYPING));
			_isTyping = true;
		}};
	}

	private ActionListener chatActionListener(final JTextField chatInput) {
		return new ActionListener() { @Override public void actionPerformed(ActionEvent ignored) {
			final Message message = new Message(chatInput.getText());
			chatInput.setText("");
				
			Threads.startDaemon(new Runnable() { @Override public void run() {
				_messageOutput.consume(message);
				appendToChatText(translate("Me"), message);
				_isTyping = false;
			}});
		}};
	}

	private JTextArea createChatText() {
		final JTextArea chatArea = new JTextArea();
		chatArea.setEditable(false);
		
		return chatArea;
	}
	
	private void appendToChatText(final String entry) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_chatText.append(entry + "\n");
				_chatText.setCaretPosition(_chatText.getDocument().getLength());		
			}
		
		});		
	}

	
	private static final long serialVersionUID = 1L;
}
