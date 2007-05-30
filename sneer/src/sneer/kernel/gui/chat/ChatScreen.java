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
	private static final long serialVersionUID = 1L;
	
	private final Omnivore<ChatEvent> _eventsReceived;
	private final Omnivore<ChatEvent> _eventsToSend;

	private JTextArea _chatArea;

	public ChatScreen(Omnivore<ChatEvent> chatEventsReceived){
		_eventsReceived = chatEventsReceived;
		
		initComponents();
		
		_eventsToSend = new Omnivore<ChatEvent>() {
			
			@Override
			public void consume(ChatEvent chatEvent) {
				appendTextToChatArea("Other: " + chatEvent._text);	
			}
		
		};
		
		setTitle("Chat");
		setVisible(true);
	}

	private void initComponents() {
		
		setLayout(new BorderLayout());
		
		_chatArea = createChatArea();
		final JTextField chatInput = createChatInput();
		
		add(new JScrollPane(_chatArea), BorderLayout.CENTER);
		add(chatInput, BorderLayout.SOUTH);
		
		
		setSize(300, 200);
	}

	private JTextField createChatInput() {
		final JTextField chatInput = new JTextField();
		chatInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChatEvent chatEvent = new ChatEvent(chatInput.getText());
				_eventsReceived.consume(chatEvent);
				appendTextToChatArea("Me: " + chatEvent._text);
				chatInput.setText("");
			}
		});
		return chatInput;
	}

	private JTextArea createChatArea() {
		final JTextArea chatArea = new JTextArea();
		chatArea.setFocusable(false);
		chatArea.setEditable(false);
		
		return chatArea;
	}
	
	public Omnivore<ChatEvent> eventsToSend(){
		return _eventsToSend;
	}
	
	private synchronized void appendTextToChatArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_chatArea.append(text + "\n");
				_chatArea.setCaretPosition(_chatArea.getDocument().getLength());		
			}
		
		});		
	}

	public static void main(String[] args) {
		
		final ChatScreen chatScreen = new ChatScreen(new Omnivore<ChatEvent>() {
			@Override
			public void consume(ChatEvent chatEvent) {
				System.out.println(chatEvent._text);
			}
		});
		
		Threads.startDaemon(new Runnable() {
			@Override
			public void run() {
				while (true){
					chatScreen.eventsToSend().consume(new ChatEvent("ping " + System.currentTimeMillis()));
					Threads.sleepWithoutInterruptions(3000);
				}
			}
		});
	}
}
