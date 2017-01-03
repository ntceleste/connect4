package connect4.players;

import connect4.enums.*;
import connect4.events.*;
import connect4.game.*;
import java.util.*;

/**
 * This class is the core of our project. It implements the AI decision making algorithm
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public class ComputerPlayer extends Player {

	//A bunch of private variables
	private GameState _gameState;
	private PlayerChangedHandler _playerChangedHandler;
	private long _lastTurnDuration;
	private int _numberOfTurnsAnalyzed;

	/**
	 * Creates a new ComputerPlayer associated with the given GameState and PlayerID
	 * @param gameState The GameState to which this ComputerPlayer belongs
	 * @param playerID The PlayerID to be associated with this new ComputerPlayer
	 */
	public ComputerPlayer(GameState gameState, PlayerID playerID) {
		super(playerID);

		_gameState = gameState;

		_playerChangedHandler = new PlayerChangedHandler(this);
		_gameState.currentPlayerChanged.addListener(_playerChangedHandler);
	}
	
	/**
	 * This handles the entire AI algorithm and eventually takes a turn for the AI
	 */
	public void play() {
		long startTime = System.currentTimeMillis(); //Measure the startTime for time analysis
		
		int columnChosen = _chooseColumn(); //This method is basically the entire algorithm

		_gameState.getBoard().dropToken(columnChosen); //Just...Drop the token

		long endTime = System.currentTimeMillis(); //More run-time analysis
		_lastTurnDuration = endTime - startTime; //Find the difference of the startTime and endTime and then set that as our last turn duration. Simple stuff here.

		_gameState.goToNextPlayer(); //Let the game move on!
	}

	/**
	 * Gets the number of milliseconds that the AI took to decide upon its most recent turn
	 * @return Returns the number of milliseconds that the AI took to decide upon its most recent turn
	 */
	public long getLastTurnDuration() {
		return _lastTurnDuration;
	}

	/**
	 * Gets the number of turns analyzed by the AI during its most recent turn
	 * @return Returns the number of turns analyzed by the AI during its most recent turn
	 */
	public int getNumberOfTurnsAnalyzed() {
		return _numberOfTurnsAnalyzed;
	}
	
	/**
	 * This method contains almost all of the AI's algorithm for turn making. Arguably the most complicated method of the entire project
	 * @return Returns the int representing the chosen column for the AI to drop a token into
	 */
	private int _chooseColumn() {
		_numberOfTurnsAnalyzed = 0; //New turn, and we haven't analyzed anything yet
		
		Board ghostBoard = _gameState.getGhostBoard(); //Easier to do this now
		int columnChosen = -1; //-1 represents no chosen column
		int firstOpenColumn = -1; //-1 represents that no column is currently open
		int columnChosenScoreAverage = -1; //-1 represents the score average of the currently chosen average
		
		for (int column = 0; column < ghostBoard.getNumberOfColumns(); column++) { //Iterate over all of the columns of the ghost board once...
			if (!ghostBoard.dropToken(column, _playerID)) { //If the column is full, no use in even looking at this.
				continue;
			}
			
			if (firstOpenColumn == -1) { //At this point, we know the column is open. And if no open column has been found yet, at least we can know this one is
				firstOpenColumn = column;
			}
			
			if (ghostBoard.checkForWinner().equals(_playerID)) { //If the token was dropped and this player won, just choose this! No better move can possibly exist!!
				columnChosen = column;
	            ghostBoard.getTopOwnedSpace(column).setOwnerPlayerID(PlayerID.NONE);
	            break;
			}
			
			int averageScoreForColumn = 0;
			boolean containsOpponentVictory = false; //If the current column allows the opponent to win, we want to avoid that

			for (int otherPlayerColumn = 0; otherPlayerColumn < ghostBoard.getNumberOfColumns(); otherPlayerColumn++) { //Now iterate over every column again, this time for the opposing player's possible turns
                if (!ghostBoard.dropToken(otherPlayerColumn, _playerID.getOppositePlayerID())) { //If the column is full, no use in even looking at this.
                    continue;
                }
    			
    			if (ghostBoard.checkForWinner().equals(_playerID.getOppositePlayerID())) { //Have to avoid letting the other player win!
    				containsOpponentVictory = true;
    			}
                
                _numberOfTurnsAnalyzed++; //This counts as one set of turns analyzed, now
				int boardScore = _scoreGhostBoard(); //This method is a big deal
				averageScoreForColumn += boardScore; //At the end of this we'll average out this sum
				ghostBoard.getTopOwnedSpace(otherPlayerColumn).setOwnerPlayerID(PlayerID.NONE); //Erase the hypothetical move from the ghost board
			}
			
			averageScoreForColumn /= ghostBoard.getNumberOfColumns(); //Now average out the sum of the scores that will result after the other player's next turn based on this possible move
			if (!containsOpponentVictory && 
				(averageScoreForColumn > columnChosenScoreAverage || columnChosen == -1)) { //As long as this doesn't result in the other player winning and is the best option so far, this column should become the new choice
				columnChosen = column;
				columnChosenScoreAverage = averageScoreForColumn;
			}
			
            ghostBoard.getTopOwnedSpace(column).setOwnerPlayerID(PlayerID.NONE); //Erase the hypothetical move from the ghost board
		}
		
		if (columnChosen == -1) { //If no column was chosen, the AI should just go with the first open column it found
			columnChosen = firstOpenColumn;
		}
		
		return columnChosen;
	}
	
	/**
	 * Determines whether a space group is imminent, i.e. is one move away from a victory
	 * @param spaceGroup The SpaceGroup to check for imminence
	 * @return Returns true if the SpaceGroup is imminent, false if otherwise
	 */
	private boolean _isSpaceGroupImminent(SpaceGroup spaceGroup) {
		if (spaceGroup.getLength() < 2) {
			return false;
		}
		if (spaceGroup.isLocked()) {
			return false;
		}
		if (spaceGroup.isLocked()) { //If this SpaceGroup is locked then this SpaceGroup is not imminent
			return false;
		}
		
		Space positiveSpace = spaceGroup.getPositiveNextSpace();
		Space negativeSpace = spaceGroup.getNegativeNextSpace();
		
		if (spaceGroup.getLength() == 3) {
			//A SpaceGroup that has the length of 3 is imminent if either end has an empty space. i.e. _ A A A or A A A _ or even _ A A A _
			if ((positiveSpace != null && positiveSpace.isEmpty()) ||
				(negativeSpace != null && negativeSpace.isEmpty())) {
				return true;
			}
		}
		else if (spaceGroup.getLength() == 2) {
			//So, if a SpaceGroup has the length of 2, it can still be imminent in that it may have another Space with a single empty space in the middle. i.e. A A _ A is just as imminent as A A A _
			if (positiveSpace != null && positiveSpace.isEmpty()) { //This would be something like A A _ A
				Space nextPositiveSpace = positiveSpace.getAdjacentSpace(spaceGroup.getAdjacencyZone());
				if (nextPositiveSpace != null && nextPositiveSpace.getOwnerPlayerID().equals(spaceGroup.getOwnerPlayerID())) {
					return true;
				}
			}
			if (negativeSpace != null && negativeSpace.isEmpty()) { //This would be something like A _ A A
				Space nextNegativeSpace = negativeSpace.getAdjacentSpace(spaceGroup.getAdjacencyZone().getOppositeAdjacencyZone());
				if (nextNegativeSpace != null && nextNegativeSpace.getOwnerPlayerID().equals(spaceGroup.getOwnerPlayerID())) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * This method scores the entire Ghost Board to determine how each set of moves will affect the state of the game
	 * @return Returns the integer score difference of the game with the given state of the Ghost Board
	 */
	private int _scoreGhostBoard() {
		Board ghostBoard = _gameState.getGhostBoard();
		SpaceGroup[] spaceGroups = BoardAnalyzer.getSpaceGroups(ghostBoard, 2, true); //Get all SpaceGroups (locked and not) of at least size 2 (size 1 SpaceGroups are neglected as they cannot be considered imminent)
		Map<PlayerID, Integer> playerScores = new HashMap<PlayerID, Integer>(); //Score the scores of each Player
		
		//Initialize each Player's score to 0
		playerScores.put(PlayerID.PLAYER1, 0);
		playerScores.put(PlayerID.PLAYER2, 0);
		
		for (int i = 0; i < spaceGroups.length; i++) { //Iterate over each SpaceGroup found
			SpaceGroup spaceGroup = spaceGroups[i];
			if (spaceGroup.isLocked() && spaceGroup.getLength() < 4) { //If the SpaceGroup is locked and is not a winning group, then there is nothing useful about this. Continue on
				continue;
			}
			
			int scoreAddend = 0;
			if (spaceGroup.getLength() >= 4) { //If the SpaceGroup is a winning SpaceGroup, that Player needs a solid 10,000 points added to their score - very scary!
				scoreAddend = 10000;
			}
			else if (_isSpaceGroupImminent(spaceGroup)) { //If the SpaceGroup is imminent, i.e. one move away from that player winning, it is a very good SpaceGroup to have. 4,000 points!
				scoreAddend = 4000;
			}
			else { //Otherwise, just give the Player an extra 100 points for every Space in the SpaceGroup
				scoreAddend = 100 * spaceGroup.getLength();
			}
			
			int newScore = playerScores.get(spaceGroup.getOwnerPlayerID()) + scoreAddend;
			playerScores.put(spaceGroup.getOwnerPlayerID(), newScore);
		}
		
		//So now, get the two scores and find their difference. Then return that
		int score = playerScores.get(_playerID);
		int opposingScore = playerScores.get(_playerID.getOppositePlayerID());
		int scoreDifference = score - opposingScore;
		
		return scoreDifference;
	}
	
	/**
	 * This class is used to handle the PlayerChanged event and start the AI's decision making algorithm
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 */
	private class PlayerChangedHandler implements IEventListener<GameState.PlayerChangedEventData> {
		private ComputerPlayer _player;

		private PlayerChangedHandler(ComputerPlayer player) {
			_player = player;
		}

		@Override
		public void handleNotification(GameState.PlayerChangedEventData eventData) {
			if (eventData.newPlayerID != _player.getPlayerID()) {
				return;
			}
			_player.play();
		}
	}

}