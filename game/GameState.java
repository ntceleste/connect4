package connect4.game;

import connect4.enums.*;
import connect4.events.*;
import connect4.guis.*;
import connect4.players.*;
import java.util.*;

/**
 * Is used to track the current state of the game
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public class GameState {

	//These events are used by other classes to handle the changes in the game state properly
	public EventSource<PlayerChangedEventData> currentPlayerChanged;
	public EventSource<GameStartedEventData> gameStarted;
	public EventSource<GameOverEventData> gameEnded;

	//Required private values, each of which has some sort of getter.
	private Board _board;
	private Board _ghostBoard;
	private boolean _isGameGoing;
	private GhostBoardManager _ghostBoardManager;
	private Map<PlayerID, Player> _players;
	private PlayerID _currentPlayerID;
	
	/**
	 * Essentially just instantiates all of the required items for the game
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 */
	public GameState() {
		_board = new Board(this);
		_ghostBoard = new Board(this);

		currentPlayerChanged = new EventSource<PlayerChangedEventData>();
		gameStarted = new EventSource<GameStartedEventData>();
		gameEnded = new EventSource<GameOverEventData>();

		_currentPlayerID = PlayerID.PLAYER1;
		_isGameGoing = false;

		_createPlayers();

		_ghostBoardManager = new GhostBoardManager();
		_board.spaceChanged.addListener(_ghostBoardManager);
	}

	/**
	 * Determines whether a game is currently in progress
	 * @return Returns true if the game is going, false if otherwise
	 */
	public boolean isGameGoing() {
		return _isGameGoing;
	}

	/**
	 * Gets the PlayerID of the current player
	 * @return Returns the PlayerID of the current player
	 */
	public PlayerID getCurrentPlayerID() {
		return _currentPlayerID;
	}

	/**
	 * Gets the Board object for this GameState
	 * @return Returns the main Board for the game
	 */
	public Board getBoard() {
		return _board;
	}

	/**
	 * Gets the Ghost Board object for this GameState
	 * @return Returns the 'ghost' Board for the game, useful for the ComputerPlayer decision making algorithm
	 */
	public Board getGhostBoard() {
		return _ghostBoard;
	}

	/**
	 * Gets the Player object associated with the current PlayerID
	 * @return Returns the Player object who is currently up.
	 */
	public Player getCurrentPlayer() {
		return getPlayer(_currentPlayerID);
	}

	/**
	 * Gets the Player object associated with the given PlayerID
	 * @param playerID The PlayerID of the player desired
	 * @return Returns the Player object associated with the PlayerID given
	 */
	public Player getPlayer(PlayerID playerID) {
		return _players.get(playerID);
	}

	/**
	 * Starts the game and fires the gameStarted event
	 */
	public void startGame() {
		_isGameGoing = true;
		gameStarted.notifyListeners(null);
	}

	/**
	 * Ends the game and fires the gameEnded event
	 * @param winnerPlayerID The PlayerID of the player who won the game
	 */
	public void endGame(PlayerID winnerPlayerID) {
		_isGameGoing = false;

		GameOverEventData eventData = new GameOverEventData(winnerPlayerID);
		gameEnded.notifyListeners(eventData);
	}

	/**
	 * Sets the current player to the player who is next up.
	 */
	public void goToNextPlayer() {
	    PlayerID winningPlayer = _board.checkForWinner();
	    if(winningPlayer == PlayerID.NONE) {

            if (_currentPlayerID == PlayerID.PLAYER1)
                _currentPlayerID = PlayerID.PLAYER2;
            else
                _currentPlayerID = PlayerID.PLAYER1;

            PlayerChangedEventData eventData = new PlayerChangedEventData(_currentPlayerID);
            currentPlayerChanged.notifyListeners(eventData);
        }
        else {
	        _currentPlayerID = PlayerID.NONE;
            this.endGame(winningPlayer);
        }
	}

	/**
	 * Creates the players, Player1 and Player2, and stores them in the _players Map.
	 */
	private void _createPlayers() {
		_players = new HashMap<PlayerID, Player>();
		_players.put(PlayerID.PLAYER1, new HumanPlayer(PlayerID.PLAYER1));
		_players.put(PlayerID.PLAYER2, new ComputerPlayer(this, PlayerID.PLAYER2));
	}

	/**
	 * Used for sending data regarding the change of the current player over the currentPlayerChanged event
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 */
	public class PlayerChangedEventData extends EventData {
		public PlayerID newPlayerID;

		public PlayerChangedEventData(PlayerID playerID) {
			newPlayerID = playerID;
		}
	}

	/**
	 * Used for sending data regarding the start of the game over the gameStarted event
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 */
	public class GameStartedEventData extends EventData {
	}

	/**
	 * Used for sending data regarding the end of the game over the gameEnded event
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 */
	public class GameOverEventData extends EventData {
		public PlayerID winnerPlayerID;

		public GameOverEventData(PlayerID playerID) {
			winnerPlayerID = playerID;
		}
	}

	/**
	 * Used for making sure the GhostBoard always matches the main Board
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 */
	private class GhostBoardManager implements IEventListener<Board.SpaceChangedEventData>{

		@Override
		public void handleNotification(Board.SpaceChangedEventData data){
			Space space = _ghostBoard.getSpace(data.column, data.row);
			space.setOwnerPlayerID(data.ownerPlayerID);
		}
	}

	/**
	 * Starts everything up.
	 * @param args Just the general args for a main method...Not used
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args){
		GameState gameState = new GameState();
		Connect4Gui gui = new Connect4Gui(gameState);
		gameState.startGame();
	}
	
}