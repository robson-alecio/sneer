package sneer.games.mediawars.mp3sushi;

import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class Game {

		public static final String WAITING_PLAYERS = "Waiting players";
		public static final String JOINED = "Joined";
		public static final String PLAYING = "Playing";
		public static final String FINISHED = "Finished";
		public static final String NOT_PLAYING = "Not Playing";
		public static final String NO_RESPONSE = "No Response";
		
		protected final Source<String> _status = new SourceImpl<String>(NO_RESPONSE);
		protected final Source<GameConfiguration> _gameConfiguration = new SourceImpl<GameConfiguration>(null);
		
		public Source<String> getStatus() {
			return _status;
		}
		public Source<GameConfiguration> getGameConfiguration() {
			return _gameConfiguration;
		}

		
}
