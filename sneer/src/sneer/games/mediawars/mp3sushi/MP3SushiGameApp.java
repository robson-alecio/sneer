package sneer.games.mediawars.mp3sushi;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.JavaLayerUtils;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import sneer.games.mediawars.mp3sushi.gui.MP3SushiGameBattleFrame;
import sneer.games.mediawars.mp3sushi.gui.MP3SushiGameStartUpFrame;
import sneer.games.mediawars.mp3sushi.gui.MP3SushiGameWaitingPlayersFrame;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class MP3SushiGameApp {

	public static String STATUS_REQUEST = "StatusRequest";
	
	private Channel _channel;
	private ListSignal<ContactAttributes> _contactsAttributes;
	private MyGame _myGame ;
	private final Map<ContactId, TheirsGame> _contactStatus = new HashMap<ContactId, TheirsGame>();	
	private final Map<ContactId, Long> _statusReportTime = new HashMap<ContactId, Long>();
	private ListSource<TheirsGame> _games = new ListSourceImpl<TheirsGame>();
	private MP3SushiGameStartUpFrame _frameStartUp;
	private MP3SushiGameWaitingPlayersFrame _frameWaitingPlayer;
	private User _user;
	private ArrayList<ID3Summary> _id3Summaries;
	private byte[] _nextMp3s;
	private Mp3SushiRoundProvider _mp3SushiRoundProvider;
	private AdvancedPlayer _player;
	private MP3SushiGameBattleFrame _gameBattle;
	private long _roundStartTime;
	private int _answers;
	private ID3Summary _actualAnswer;
	private int _mp3sReceive = 0;

	public MP3SushiGameApp(Signal<String> ownName, User user, Channel channel, ListSignal<ContactAttributes> contactsAttributes) {
		_myGame = new MyGame(ownName);
		_user = user;
		_channel = channel;
		_contactsAttributes = contactsAttributes;
		_channel.input().addReceiver(messageReceiver());
		_myGame.getStatus().output().addTransientReceiver(statusBroadcaster());
		_myGame.getGameConfiguration().output().addTransientReceiver(configurationBroadcaster());
		askStatusFromContacts();
		JavaLayerUtils.setHook(new JLayerResourceHook());
	}

	private void askStatusFromContacts() {
		int size = _contactsAttributes.currentSize();
		for (int i = 0; i < size; i++) {
			ContactAttributes contactAttributes = _contactsAttributes.currentGet(i);
			ContactId id = contactAttributes.id();
			TheirsGame game = new TheirsGame(contactAttributes);
			addGameForContatct(id, game);
			requestStatus(id); 
		}
	}

	private void requestStatus(ContactId id) {
		Long nanoTime = System.nanoTime();
		Long lastReportTime = _statusReportTime.get(id);
		if ((lastReportTime != null) && ((nanoTime - lastReportTime) < 10*1000000000)) return;
		sendGameMessage(id, GameMessage.STATUS_REQUEST, null);
	}

	private void addGameForContatct(ContactId id, TheirsGame game) {
		_contactStatus.put(id, game);
		_games.add(game);
	}

	private Omnivore<Packet> messageReceiver() {
		return new Omnivore<Packet>() { @SuppressWarnings("unchecked")
		public void consume(Packet packet) {
			ContactId contactiD = packet._contactId;
			GameMessage message = (GameMessage) packet._contents;
			System.out.println(message.getType());
			// Não sei se poderia criar conteudos que se executassem, evitando os ifs. Medo de segurança"
			if (GameMessage.STATUS_REQUEST.equals(message.getType())) {
				sendGameMessage(contactiD, GameMessage.STATUS_REPORT,_myGame.getStatus().output().currentValue());
				if (_myGame.getGameConfiguration().output().currentValue() != null)
					sendGameMessage(contactiD, GameMessage.CONFIGURATION_REPORT,_myGame.getGameConfiguration().output().currentValue());
				requestStatus(contactiD);
				return;
			} 
			if (GameMessage.STATUS_REPORT.equals(message.getType())) {
				Game game = _contactStatus.get(contactiD);
				String status = (String)message.getContent();
				if (!(status.equals(game.getStatus().output().currentValue()))) {
					game.getStatus().setter().consume(status);					
				}
				_statusReportTime.put(contactiD, System.nanoTime());
				return;
			}
			if (GameMessage.CONFIGURATION_REPORT.equals(message.getType())) {
				Game game = _contactStatus.get(contactiD);
				game.getGameConfiguration().setter().consume((GameConfiguration)message.getContent());
				_statusReportTime.put(contactiD, System.nanoTime());
				return;
			}
			if (GameMessage.JOIN_REQUEST.equals(message.getType())) {
				TheirsGame game = _contactStatus.get(contactiD);
				if (_myGame.canReceiveJoins()) {
					sendGameMessage(game.getPlayer().id(), GameMessage.JOIN_ACCEPTED, null);
					PlayerContactIdentification pi = _myGame.addPlayerFromContact(game.getPlayer());
					playerJoinAccepted(pi);
				} else {
					sendGameMessage(game.getPlayer().id(), GameMessage.JOIN_NOT_ACCEPTED, null);
				}
				return;
			}
			if (GameMessage.JOIN_ACCEPTED.equals(message.getType())) {
				TheirsGame hostGame = _contactStatus.get(contactiD);
				joinAccepted(hostGame, contactiD);
			}
			
			if (GameMessage.JOINED.equals(message.getType())) {
				_myGame.addExternalPlayer((PlayerExportInfo) message.getContent());
			}
			
			if (GameMessage.MP3S_DOMAIN.equals(message.getType())) {
				ArrayList<ID3Summary> id3s = (ArrayList<ID3Summary>) message.getContent(); 
				openBattleFrame(id3s);
			}
			if (GameMessage.NEXT_MP3.equals(message.getType())) {
				_nextMp3s = (byte[]) message.getContent();
				sendGameMessage(contactiD, GameMessage.RECEIVE_MP3, null);
			}
			if (GameMessage.RECEIVE_MP3.equals(message.getType())) {
				_mp3sReceive++;
			}
			if (GameMessage.PLAY_ROUND.equals(message.getType())) {
				new Thread() {
					@Override
					public void run() {startRound();}
				}.start();
			}
			if (GameMessage.ANSWER.equals(message.getType())) {
				PlayerContactIdentification pci = _myGame.getContactPlayer().get(contactiD);
				ID3Answer id3Answer = (ID3Answer) message.getContent();
				receiveAnswer(pci, id3Answer.getId3Summary(), id3Answer.getTime());
			}
			if (GameMessage.ANSWER_SCORE.equals(message.getType())) {
				AnswerScore as = (AnswerScore) message.getContent();
				PlayerIdentification pi = _myGame.getLongIdPlayer().get(as.getId());
				pi.setActualScore(as.getScore());
			}
		}

		private void playerJoinAccepted(PlayerContactIdentification playerJoined) {
			sendGameMessage(playerJoined.getContactId(), GameMessage.JOINED, _myGame.getHostPlayer().exportInfo());
			for ( Entry<ContactId, PlayerContactIdentification> player : _myGame.getContactPlayer().entrySet() ) {
				if (playerJoined != player.getValue()) {
					sendGameMessage(playerJoined.getContactId(), GameMessage.JOINED, player.getValue().exportInfo());
				} else {
					sendGameMessage(player.getKey(), GameMessage.JOINED, playerJoined.exportInfo());
				}
				
			}
		}
		
		
		};
	}

	private Omnivore<String> statusBroadcaster() {
		return new Omnivore<String>() { public void consume(String newStatus) {
			for (ContactId receiver : _contactStatus.keySet()) sendGameMessage(receiver, GameMessage.STATUS_REPORT,newStatus);
		}};
	}

	private Omnivore<GameConfiguration> configurationBroadcaster() {
		return new Omnivore<GameConfiguration>() { public void consume(GameConfiguration configuration) {
			for (ContactId receiver : _contactStatus.keySet()) sendGameMessage(receiver, GameMessage.CONFIGURATION_REPORT,configuration);
		}};
	}

	
	private void joinAccepted(TheirsGame hostGame, ContactId contactiD) {
		_myGame.joinAccepted(hostGame, contactiD);
		_frameStartUp.setVisible(false);
		if(_frameWaitingPlayer == null) {
			_frameWaitingPlayer = new MP3SushiGameWaitingPlayersFrame(_user,_myGame.getPlayers(),this, false);
		}
		_frameWaitingPlayer.setVisible(true);
	}

	public void start() {
		if (_frameStartUp == null) _frameStartUp = new MP3SushiGameStartUpFrame(_games.output(), this);
		_frameStartUp.setVisible(true);
	}

	public void waitingPlayerForConfiguration(GameConfiguration configuration) {
		// Implement Auto-generated method stub
		_myGame.waitingPlayerForConfiguration(configuration);
		_frameStartUp.setVisible(false);
		if(_frameWaitingPlayer == null) {
			_frameWaitingPlayer = new MP3SushiGameWaitingPlayersFrame(_user,_myGame.getPlayers(),this, true);
		}
		_frameWaitingPlayer.setVisible(true);
	}

	public void startGameWithDirectories(File[] directories) {
		_id3Summaries = new ArrayList<ID3Summary>();
		ArrayList<String> fileNames = new ArrayList<String>();
		listOfFiles(fileNames, directories);
		for (String fileName : fileNames) {
			System.out.println(fileName);
			ID3Summary id3Summary = ID3Summary.createFromFileName(fileName);
			if (id3Summary != null) _id3Summaries.add(id3Summary);
		}
		for ( Entry<ContactId, PlayerContactIdentification> player : _myGame.getContactPlayer().entrySet() ) {
			sendGameMessage(player.getKey(), GameMessage.MP3S_DOMAIN, _id3Summaries);
		}
		openBattleFrame(_id3Summaries);
		_mp3SushiRoundProvider = new Mp3SushiRoundProvider(_id3Summaries,_myGame.getGameConfiguration().output().currentValue());
		new Thread() {
			@Override
			public void run() {mainLoop();}
		}.start();
	}

	public void mainLoop() {
		
		_nextMp3s = _mp3SushiRoundProvider.nextMP3Piece();
		if (_nextMp3s == null) return;
		_answers = 0;
		_mp3sReceive = 0;
		Set<Entry<ContactId, PlayerContactIdentification>> entrySet = _myGame.getContactPlayer().entrySet();
		for ( Entry<ContactId, PlayerContactIdentification> player : entrySet ) {
			sendGameMessage(player.getKey(), GameMessage.NEXT_MP3, _nextMp3s);
		}
		while (_mp3sReceive < entrySet.size() ) {};
		for ( Entry<ContactId, PlayerContactIdentification> player : entrySet ) {
			sendGameMessage(player.getKey(), GameMessage.PLAY_ROUND, null);
		}
		startRound();
	}
	
	public void startRound() {
		for (int i = 0; i < _myGame.getPlayers().currentSize(); i++) {
			PlayerIdentification pi = _myGame.getPlayers().currentGet(i);
			pi.resetActualScore();
		}
		_gameBattle.resetCountDownFinished();
		try {
			SwingUtilities.invokeAndWait(
				new Runnable() {
					public void run() {
						_gameBattle.startCountDown();
					}
				}
			);
		} catch (InterruptedException e) {
			// Implement Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// Implement Auto-generated catch block
			e.printStackTrace();
		}
		while (!_gameBattle.isCountDownFinished()) {};
		playRound();
	}
	
	public void playRound() {
		_actualAnswer = null;
		PlayBackWait pbw = null;
		try {
			GameConfiguration g = _myGame.getGameConfiguration().output().currentValue();
			_player = new AdvancedPlayer(new BufferedInputStream(new ByteArrayInputStream(_nextMp3s)));
			pbw = new PlayBackWait();
			_player.setPlayBackListener(pbw);
			new Thread() {
				@Override
				public void run() {
					try {
						_player.play();
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
				}
			}.start();
			_roundStartTime = System.nanoTime();
			Thread.sleep((g.getSecondsOfMusic()+g.getSecondsToGuess()) * 1000);
			_player.close();
			if (_actualAnswer == null) {
				SwingUtilities.invokeAndWait(
					new Runnable() {
						public void run() {
							_gameBattle.disableGuess();							
						}
					}
				);
				setAnswer(null);
			}
			if (_mp3SushiRoundProvider != null) {
				while (_answers < _myGame.getPlayers().currentSize()) {};
				mainLoop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class PlayBackWait extends PlaybackListener {

		private boolean finished = false;
		@Override
		public void playbackStarted(PlaybackEvent arg0) {
			super.playbackStarted(arg0);
		}
		@Override
		public void playbackFinished(PlaybackEvent arg0) {
			finished = true;
			super.playbackFinished(arg0);
		}
		public boolean isFinished() {
			return finished;
		}
		
	}
	
	private void openBattleFrame(ArrayList<ID3Summary> id3sSummaries) {
		_frameWaitingPlayer.setVisible(false);
		_gameBattle = new MP3SushiGameBattleFrame(_myGame.getPlayers(),id3sSummaries, this);
		_gameBattle.setVisible(true);
	}
	
	private void listOfFiles(ArrayList<String> fileNames, File[] dirs) {
		for (int i = 0; i < dirs.length; i++) {
			File dir = dirs[i];
			listOfFiles(fileNames, dir);
		}
	}
	private void listOfFiles(ArrayList<String> fileNames, File dir) {
	    File[] list = dir.listFiles();
	    for(File f : list)
	       if(f.isDirectory()) listOfFiles(fileNames, f);
	      else if(fileNames.add(f.getAbsolutePath()));
	 }

	public void joinGame(TheirsGame game) {
		// Implement Auto-generated method stub
		if (!game.canReceiveJoins()) return;
		sendGameMessage(game.getPlayer().id(), GameMessage.JOIN_REQUEST, null);
	}

	public void quitGame() {
		// Implement Auto-generated method stub
		
	}

	private void sendGameMessage(ContactId contactId, String messageType, Object content) {
		_channel.output().consume(new Packet(contactId, new GameMessage(messageType,content)));
	}

	public void setAnswer(ID3Summary answer) {
		
		long answerTime = System.nanoTime();
		_actualAnswer = answer;
		if (_mp3SushiRoundProvider == null) {
			sendGameMessage(_myGame.getExternalHostId(), GameMessage.ANSWER, new ID3Answer(answer, answerTime - _roundStartTime));
		} else {
			receiveAnswer(_myGame.getHostPlayer(), answer, answerTime - _roundStartTime);
		}
		
	}
	
	public void receiveAnswer(PlayerIdentification playerId, ID3Summary answer, long time) {
		GameConfiguration gc = _myGame.getGameConfiguration().output().currentValue();
		int score = 0;
		if (_mp3SushiRoundProvider.isGoodAnswer(answer)) {
			int timeInSeconds = (int) (time / 1000000000);
			score = gc.getSecondsOfMusic() + gc.getSecondsToGuess() - timeInSeconds;
			playerId.setActualScore(score);
		}
		for ( Entry<ContactId, PlayerContactIdentification> player : _myGame.getContactPlayer().entrySet() ) {
			sendGameMessage(player.getKey(), GameMessage.ANSWER_SCORE, new AnswerScore(playerId.getId(), score));
		}
		_answers++;	
	}

}

