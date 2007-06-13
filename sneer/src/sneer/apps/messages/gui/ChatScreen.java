package sneer.apps.messages.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import sneer.apps.messages.ChatEvent;

import wheel.io.Log;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;
import wheel.reactive.SourceImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class ChatScreen extends JFrame {
	
	public ChatScreen(Signal<String> otherGuysNick, final ListSignal<ChatEvent> chatEvents, Consumer<ChatEvent> sender){
		_otherGuysNick = otherGuysNick;
		_chatSender = sender;
		
		initComponents();
		
		otherGuysNick.addReceiver(new Receiver<String>() {
			@Override
			public void receive(String valueChange) {
				setTitle(valueChange);
			}
		});
		
		chatEvents.addListReceiver(new SimpleListReceiver() {
			@Override
			public void elementAdded(int index) {
				ChatEvent chatEvent = chatEvents.currentGet(index);
				appendToChatText("To " + chatEvent._destination + ": " + chatEvent._text);
			}
		});
		
		setVisible(true);
	}

	
	private final Signal<String> _otherGuysNick;
	private final Consumer<ChatEvent> _chatSender;
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
				final ChatEvent chatEvent = new ChatEvent(chatInput.getText(), _otherGuysNick.currentValue());
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
	
	public Omnivore<ChatEvent> chatEventReceiver(){
		return new Omnivore<ChatEvent>() {
			@Override
			public void consume(ChatEvent chatEvent) {
				appendToChatText(_otherGuysNick.currentValue() + ": " + chatEvent._text);	
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

	
	private static final long serialVersionUID = 1L;
}
