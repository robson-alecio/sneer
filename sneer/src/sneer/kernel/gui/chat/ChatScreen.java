package sneer.kernel.gui.chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import sneer.kernel.business.chat.ChatEvent;

import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class ChatScreen extends JFrame {
	
	public ChatScreen(String otherGuysNick, Omnivore<ChatEvent> chatEventSender){
		_otherGuysNick = otherGuysNick;
		_chatSender = chatEventSender;
		
		initComponents();
		setTitle(otherGuysNick);
		setVisible(true);
	}

	
	private final String _otherGuysNick;
	private final Omnivore<ChatEvent> _chatSender;
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
				ChatEvent chatEvent = new ChatEvent(chatInput.getText());
				chatInput.setText("");
				_chatSender.consume(chatEvent);
				appendToChatText("Me: " + chatEvent._text);
			}
		});
		return chatInput;
	}

	private JTextArea createChatText() {
		final JTextArea chatArea = new JTextArea();
		chatArea.setFocusable(true);
		chatArea.setEditable(false);
		
		return chatArea;
	}
	
	public Omnivore<ChatEvent> chatEventReceiver(){
		return new Omnivore<ChatEvent>() {
			@Override
			public void consume(ChatEvent chatEvent) {
				appendToChatText(_otherGuysNick + ": " + chatEvent._text);	
			}
		};
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

	public static void main(String[] args) {
		
		final ChatScreen chatScreen = new ChatScreen("Depressed Robot", new Omnivore<ChatEvent>() {
			@Override
			public void consume(ChatEvent chatEvent) {
				System.out.println(chatEvent._text);
			}
		});
		
		Threads.startDaemon(new Runnable() {
			@Override
			public void run() {
				while (true) {
					chatScreen.chatEventReceiver().consume(new ChatEvent("life sucks " + System.currentTimeMillis()));
					Threads.sleepWithoutInterruptions(3000);
				}
			}
		});
	}
	
	private static final long serialVersionUID = 1L;
}
