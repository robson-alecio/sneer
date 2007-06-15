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
import wheel.reactive.Receiver;
import wheel.reactive.Signal;

public class ConversationScreen extends JFrame {
	
	public ConversationScreen(Signal<String> otherGuysNick, final Signal<Object> lastIncomingMessage, Consumer<Object> sender){
		_otherGuysNick = otherGuysNick;
		_chatSender = sender;
		
		initComponents();
		
		otherGuysNick.addReceiver(new Receiver<String>() {
			@Override
			public void receive(String valueChange) {
				setTitle(valueChange);
			}
		});
		
		lastIncomingMessage.addReceiver(new Receiver<Object>() {
			@Override
			public void receive(Object messageObj) {
				Message message = (Message)messageObj; //Refactor Try to eliminate casting.
				appendToChatText("To " + message._destination + ": " + message._text);
			}
		});
		
		setVisible(true);
	}

	
	private final Signal<String> _otherGuysNick;
	private final Consumer<Object> _chatSender;
	private final JTextArea _chatText = createChatText();

	
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
				final Message chatEvent = new Message(chatInput.getText(), _otherGuysNick.currentValue());
				chatInput.setText("");
				
				Threads.startDaemon(new Runnable() {		
					@Override
					public void run() {
						try {
							_chatSender.consume(chatEvent);
						} catch (IllegalParameter e) {
							Log.log(e);
							//Fix: the nick of the contact might have changed just before the event was consumed. Use some sort of timestamp associated with the nick.
						}
					}
				});
				
			}
		});
		return chatInput;
	}

	private JTextArea createChatText() {
		final JTextArea chatArea = new JTextArea();
		chatArea.setEditable(false);
		
		return chatArea;
	}
	
	private void appendToChatText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_chatText.append(text + "\n");
				_chatText.setCaretPosition(_chatText.getDocument().getLength());		
			}
		
		});		
	}

	
	private static final long serialVersionUID = 1L;
}
