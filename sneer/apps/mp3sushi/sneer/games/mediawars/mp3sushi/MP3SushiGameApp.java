package sneer.games.mediawars.mp3sushi;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;

import javazoom.jl.decoder.JavaLayerUtils;
import sneer.games.mediawars.mp3sushi.gui.MP3SushiGameBattleFrame;
import sneer.games.mediawars.mp3sushi.gui.MP3SushiGameStartUpFrame;
import sneer.games.mediawars.mp3sushi.gui.MP3SushiGameWaitingPlayersFrame;
import sneer.games.mediawars.mp3sushi.player.PlayerContactIdentification;
import sneer.games.mediawars.mp3sushi.player.PlayerExportInfo;
import sneer.games.mediawars.mp3sushi.player.PlayerIdentification;
import sneer.games.mediawars.mp3sushi.round.AnswerScore;
import sneer.games.mediawars.mp3sushi.round.MP3SushiClientRound;
import sneer.games.mediawars.mp3sushi.round.MP3SushiHostRound;
import sneer.games.mediawars.mp3sushi.round.MP3SushiRound;
import sneer.games.mediawars.mp3sushi.round.Mp3PieceProvider;
import sneer.games.mediawars.mp3sushi.util.CaseGameMessage;
import sneer.games.mediawars.mp3sushi.util.CaseGameMessageConsume;
import sneer.kernel.api.SovereignApplicationNeeds;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.io.ui.Action;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
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
	private MP3SushiGameBattleFrame _gameBattle;
	private ArrayList<ID3Summary> _id3Summaries; 
	private MP3SushiRound _mp3SushiRound;
	private CaseGameMessage _caseGameMessage = new CaseGameMessage();

	//public MP3SushiGameApp(Signal<String> ownName, User user, Channel channel, ListSignal<ContactAttributes> contactsAttributes) {
	public MP3SushiGameApp(SovereignApplicationNeeds config) {
		_myGame = new MyGame(config.ownName());
		_user = config.user();
		_channel = config.channel();
		_contactsAttributes = config.contactAttributes();
		_channel.input().addReceiver(messageReceiver());
		_myGame.getStatus().output().addTransientReceiver(statusBroadcaster());
		_myGame.getGameConfiguration().output().addTransientReceiver(configurationBroadcaster());
		askStatusFromContacts();
		initCaseGameMessage();
		JavaLayerUtils.setHook(new JLayerResourceHook());
	}

	private void askStatusFromContacts() {
		int size = _contactsAttributes.currentSize();
		for (int i = 0; i < size; i++) {
			ContactAttributes contactAttributes = _contactsAttributes.currentGet(i);
			ContactId id = contactAttributes.id();
			TheirsGame game = new TheirsGame(contactAttributes);
			_contactStatus.put(id, game);
			_games.add(game);
			requestStatus(id); 
		}
	}

	public void start() {
		SwingUtilities.invokeLater(new Thread() {
			@Override
			public void run() {
				if (_frameStartUp == null) _frameStartUp = new MP3SushiGameStartUpFrame(_games.output(), MP3SushiGameApp.this);
				_frameStartUp.setVisible(true);
			}	
		});
	}

	private void requestStatus(ContactId id) {
		Long nanoTime = System.nanoTime();
		Long lastReportTime = _statusReportTime.get(id);
		if ((lastReportTime != null) && ((nanoTime - lastReportTime) < 10*1000000000)) return;
		sendGameMessage(id, GameMessage.STATUS_REQUEST, null);
	}

	private Omnivore<Packet> messageReceiver() {
		return new Omnivore<Packet>() { @SuppressWarnings("unchecked")
			public void consume(Packet packet) {
				_caseGameMessage.switchCasePacket(packet);
			}
		};
	}

	
	private MP3SushiHostRound mp3SushiHostRound() { return (MP3SushiHostRound) _mp3SushiRound; }
	
	private MP3SushiClientRound mp3SushiClientRound() { return (MP3SushiClientRound) _mp3SushiRound; }	
	
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
		_myGame.setPlaying();
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

		GameConfiguration gc = _myGame.getGameConfiguration().output().currentValue();
		Mp3PieceProvider mp3SushiRoundProvider = new Mp3PieceProvider(_id3Summaries,gc.getType(), gc.getSecondsOfMusic());
		openBattleFrame(_id3Summaries);
		_mp3SushiRound = new MP3SushiHostRound(this,_gameBattle,_myGame, mp3SushiRoundProvider);
		_gameBattle.setMp3SushiRound(_mp3SushiRound);
		_mp3SushiRound.start();
	}

	
	
	private void openBattleFrame(ArrayList<ID3Summary> id3sSummaries) {
		_frameWaitingPlayer.setVisible(false);
		_gameBattle = new MP3SushiGameBattleFrame(_myGame.getPlayers(),id3sSummaries);
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
	      else fileNames.add(f.getAbsolutePath());
	 }

	public void joinGame(TheirsGame game) {
		// Implement Auto-generated method stub
		if (!game.canReceiveJoins()) return;
		sendGameMessage(game.getPlayer().id(), GameMessage.JOIN_REQUEST, null);
	}

	private void startGameForClient(ArrayList<ID3Summary> id3s) {
		_myGame.setPlaying();
		openBattleFrame(id3s);
		_mp3SushiRound = new MP3SushiClientRound(MP3SushiGameApp.this,_gameBattle,_myGame);
		_gameBattle.setMp3SushiRound(_mp3SushiRound);
		_mp3SushiRound.start();
	}

	public void quitGame() {
//		if (_myGame.canReceiveJoins() || (_mainLoopThread != null)) {
//			if (_mainLoopThread != null) _mainLoopThread.interrupt();
//			for ( Entry<ContactId, PlayerContactIdentification> player : _myGame.getContactPlayer().entrySet() ) {
//				sendGameMessage(player.getKey(), GameMessage.HOST_QUIT, null);
//			}
//			
//		} else {
//			sendGameMessage(_myGame.getExternalHostId(), GameMessage.PLAYER_QUIT, null);
//		}
//		quitFrame();
	}

	public void showStartUpFrame() {
		_myGame.quit();
		if (_frameWaitingPlayer != null) _frameWaitingPlayer.setVisible(false);
		if (_gameBattle != null) _gameBattle.setVisible(false);
		_frameStartUp.setVisible(true);
	}
	
	public void sendGameMessage(ContactId contactId, String messageType, Object content) {
		_channel.output().consume(new Packet(contactId, new GameMessage(messageType,content)));
	}

	

	public void initCaseGameMessage() {
		
		_caseGameMessage.addCase(GameMessage.STATUS_REQUEST, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				sendGameMessage(contactiD, GameMessage.STATUS_REPORT,_myGame.getStatus().output().currentValue());
				if (_myGame.getGameConfiguration().output().currentValue() != null)
					sendGameMessage(contactiD, GameMessage.CONFIGURATION_REPORT,_myGame.getGameConfiguration().output().currentValue());
				requestStatus(contactiD);
			}
		});

		_caseGameMessage.addCase(GameMessage.STATUS_REPORT, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				Game game = _contactStatus.get(contactiD);
				String status = (String) content;
				if (!(status.equals(game.getStatus().output().currentValue()))) {
					game.getStatus().setter().consume(status);					
				}
				_statusReportTime.put(contactiD, System.nanoTime());
			}
		});

		_caseGameMessage.addCase(GameMessage.CONFIGURATION_REPORT, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				Game game = _contactStatus.get(contactiD);
				game.getGameConfiguration().setter().consume((GameConfiguration)content);
				_statusReportTime.put(contactiD, System.nanoTime());
			}
		});
		
		_caseGameMessage.addCase(GameMessage.JOIN_REQUEST, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				TheirsGame game = _contactStatus.get(contactiD);
				if (_myGame.canReceiveJoins()) {
					sendGameMessage(game.getPlayer().id(), GameMessage.JOIN_ACCEPTED, null);
					PlayerContactIdentification pi = _myGame.addPlayerFromContact(game.getPlayer());
					playerJoinAccepted(pi);
				} else {
					sendGameMessage(game.getPlayer().id(), GameMessage.JOIN_NOT_ACCEPTED, null);
				}
			}
		});

		_caseGameMessage.addCase(GameMessage.JOIN_ACCEPTED, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				TheirsGame hostGame = _contactStatus.get(contactiD);
				joinAccepted(hostGame, contactiD);
			}
		});
		
		_caseGameMessage.addCase(GameMessage.JOINED, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				_myGame.addExternalPlayer((PlayerExportInfo) content);
			}
		});

		_caseGameMessage.addCase(GameMessage.HOST_QUIT, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				mp3SushiClientRound().quitGame();
			}
		});

		_caseGameMessage.addCase(GameMessage.PLAYER_QUIT, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				_myGame.removePlayerFromContactId(contactiD);
			}
		});

		_caseGameMessage.addCase(GameMessage.MP3S_DOMAIN, new CaseGameMessageConsume() {
			@SuppressWarnings("unchecked")
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				startGameForClient((ArrayList<ID3Summary>) content);
			}

		});

		_caseGameMessage.addCase(GameMessage.NEXT_MP3, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				mp3SushiClientRound().receiveNextMP3((byte[]) content);
			}
		});

		_caseGameMessage.addCase(GameMessage.RECEIVE_MP3, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				mp3SushiHostRound().receiveMP3();
			}
		});

		_caseGameMessage.addCase(GameMessage.PLAY_ROUND, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				mp3SushiClientRound().roundStart();
			}
		});

		_caseGameMessage.addCase(GameMessage.ANSWER, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				PlayerContactIdentification pci = _myGame.getContactPlayer().get(contactiD);
				ID3Answer id3Answer = (ID3Answer) content;
				mp3SushiHostRound().receiveAnswer(pci, id3Answer.getId3Summary(), id3Answer.getTime());
			}
		});

		_caseGameMessage.addCase(GameMessage.ANSWER_SCORE, new CaseGameMessageConsume() {
			@Override
			public void consume(String type, ContactId contactiD, Object content) {
				AnswerScore as = (AnswerScore) content;
				PlayerIdentification pi = _myGame.getLongIdPlayer().get(as.getId());
				pi.setActualScore(as.getScore());

			}
		});

	}
	
	public List<Action> mainActions() {
		return Collections.singletonList((Action)new MP3SushiMainAction(this));
	}
		
}

