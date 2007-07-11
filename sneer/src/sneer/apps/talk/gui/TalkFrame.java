package sneer.apps.talk.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.quilombo.audio.AudioReceiver;
import org.quilombo.audio.AudioSender;

import sneer.apps.conversations.Message;
import sneer.apps.talk.AudioPacket;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class TalkFrame extends JFrame {
	private AudioReceiver _audioReceiver;
	private AudioSender _audioSender;
	private PipedOutputStream _outSpeaker;
	private PipedInputStream _inSpeaker;
	private PipedOutputStream _outMicrophone;
	private PipedInputStream _inMicrophone;

	public TalkFrame(Signal<String> otherGuysNick, final Signal<AudioPacket> audioInput, Omnivore<AudioPacket> audioOutput){
		_otherGuysNick = otherGuysNick;
		_audioOutput = audioOutput;
		
		initComponents();
		
		_otherGuysNick.addReceiver(new Omnivore<String>() { @Override public void consume(String nick) {
			setTitle(nick);
		}});
		
		audioInput.addReceiver(new Omnivore<AudioPacket>() { @Override public void consume(AudioPacket audioPacket) {
			String nick = _otherGuysNick.currentValue();
			
			// audio received... play it...
			
		}});
		
		initAudio();
		
		setVisible(true);
	}

	private final Signal<String> _otherGuysNick;
	private final Omnivore<AudioPacket> _audioOutput;
	
	private final JToggleButton mute = new JToggleButton("mute");
	
	private void initComponents() {
		setLayout(new BorderLayout());
		
		add(mute, BorderLayout.CENTER);
		
		setSize(100,50);
		
	}

	private void initAudio() {
		try{
		_outSpeaker = new PipedOutputStream();
        _inSpeaker = new PipedInputStream(_outSpeaker);
        
        _inMicrophone = new PipedInputStream();
        _outMicrophone = new PipedOutputStream(_inMicrophone);
        
        _audioReceiver = new AudioReceiver(_inSpeaker);
        _audioReceiver.start();
        _audioReceiver.waitUntilRunning();
        
        _audioSender = new AudioSender(_outMicrophone);
        _audioSender.start();
        _audioSender.waitUntilRunning();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		//just to test it... loopback from mic to speaker
		Threads.startDaemon(new Runnable() { @Override public void run() {
			byte[] buffer = new byte[1024];
			try{
				while(true){
					int readed = _inMicrophone.read(buffer,0,buffer.length);
					_outSpeaker.write(buffer,0,readed);
					_outSpeaker.flush();
				}
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}});
		
	}
	
	private void sendAudio(byte[] contents){
		//check mute button and send it...
		final AudioPacket audioPacket = new AudioPacket(contents);
		Threads.startDaemon(new Runnable() { @Override public void run() {
			_audioOutput.consume(audioPacket);
		}});
		
	}

	private static final long serialVersionUID = 1L;
}
