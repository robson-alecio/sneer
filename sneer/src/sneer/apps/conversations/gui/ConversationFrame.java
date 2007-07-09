package sneer.apps.conversations.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import sneer.apps.conversations.Message;
import wheel.io.Log;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class ConversationFrame extends JFrame {
	
	public ConversationFrame(Signal<String> otherGuysNick, final Signal<Message> messageInput, Omnivore<Message> messageOutput){
		_otherGuysNick = otherGuysNick;
		_messageOutput = messageOutput;
		
		initComponents();
		
//		_otherGuysNick.addReceiver(new Omnivore<String>() { @Override public void consume(String nick) {
//			setTitle(nick);
//		}});
		
		messageInput.addReceiver(new Omnivore<Message>() { @Override public void consume(Message message) {
//			appendToChatText(_otherGuysNick.currentValue(), message);
			appendToChatText("nick", message);
		}});
		
		setVisible(true);
	}

	private final Signal<String> _otherGuysNick;
	private final Omnivore<Message> _messageOutput;
	private final JTextArea _chatText = createChatText();

	private void appendToChatText(String sender, Message message) {
		appendToChatText(sender + ": " + message._text);
	}
	
	private void initComponents() {
		setLayout(new BorderLayout());
		
		add(new JScrollPane(_chatText), BorderLayout.CENTER);
		add(createChatInput(), BorderLayout.SOUTH);
		
		setSize(300, 200);
	}

	private JTextField createChatInput() {
		final JTextField chatInput = new JTextField();
		chatInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ignored) {
				final Message message = new Message(chatInput.getText());
				chatInput.setText("");
				
				Threads.startDaemon(new Runnable() { @Override public void run() {
					_messageOutput.consume(message);
					appendToChatText("Me: ", message);
				}});
			}
		});
		return chatInput;
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
