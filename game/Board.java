package connect4.game;

import connect4.enums.*;
import connect4.events.*;

/**
 * This class holds all the Space nodes and manages their relations to one another.
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public class Board {

	//The dimensions of the game
	private final int _COLUMNS = 7;
	private final int _ROWS = 6;
	
	//Gotta keep track of these two things.
	private GameState _gameState;
	private Space[][] _spaces; //The basis for our graph
	
	//Super useful event for after a space is changed
	public EventSource<SpaceChangedEventData> spaceChanged;
	
	/**
	 * Creates a new Board associated with the given GameState
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 * @param gameState The GameState that this will belong to
	 */
	public Board(GameState gameState) {
		_gameState = gameState;

		spaceChanged = new EventSource<SpaceChangedEventData>();

		_createSpaces();
	}

	/**
	 * Gets the number of columns
	 * @return Returns the number of columns
	 */
	public int getNumberOfColumns() {
		return _COLUMNS;
	}

	/**
	 * Gets the number of rows
	 * @return Returns the number of rows
	 */
	public int getNumberOfRows() {
		return _ROWS;
	}
	
	/**
	 * Gets the Space at the specified column and row
	 * @param column The column of the Space desired
	 * @param row The row of the Space desired
	 * @return The Space at the BoardCoordinate (column, row)
	 */
	public Space getSpace(int column, int row) {
		return _spaces[column][row];
	}
	
	/**
	 * This method handles giving the top available Space of a column to the Player identified by the given PlayerID
	 * @param column The column to drop a token into
	 * @param playerID The PlayerID to set the owner of the top Space to
	 * @return Returns true if the column was open, false if it was already full
	 */
	public boolean dropToken(int column, PlayerID playerID) {
		Space[] spacesColumn = _spaces[column];
		
		if (!spacesColumn[0].isEmpty()) {
			return false;
		}
		
		int emptyRow = 0;

		for (int i = 1; i < spacesColumn.length && spacesColumn[i].isEmpty(); i++) {
			emptyRow++;
		}

		Space space = spacesColumn[emptyRow];
		space.setOwnerPlayerID(playerID);

		SpaceChangedEventData eventData = new SpaceChangedEventData(column, emptyRow, playerID);
		spaceChanged.notifyListeners(eventData);

		return true;
	}

	/**
	 * Same as dropToken(int, PlayerID), but assumes that it should use the GameState's currentPlayerID
	 * @param column The column to drop a token into
	 * @return Returns true if the column was open, false if it was already full
	 */
	public boolean dropToken(int column) {
		return dropToken(column, _gameState.getCurrentPlayerID());
	}
	
	/**
	 * This method uses the BoardAnalyzer to determine if there are any SpaceGroups of size 4
	 * @return Returns PlayerID.NONE if no player has won, otherwise it returns the PlayerID of the player who won
	 */
	public PlayerID checkForWinner() {
		SpaceGroup[] winningGroups = BoardAnalyzer.getSpaceGroups(this, 4, true);
		
		if (winningGroups.length == 0) {
			return PlayerID.NONE;
		}
		
		SpaceGroup winningGroup = winningGroups[0];
		
		return winningGroup.getOwnerPlayerID();
	}

	/**
	 * Used to find the top occupied Space in a column
	 * @param column The column to find the Space in
	 * @return Returns null if the column is completely empty, otherwise returns the top Space with an owner.
	 */
	public Space getTopOwnedSpace(int column){
		Space[] spacesColumn = _spaces[column];

		for(int rowIndex = 0; rowIndex < spacesColumn.length; rowIndex++){
			Space space = spacesColumn[rowIndex];
			if(!space.isEmpty())
				return space;
		}

		return null;
	}

	/**
	 * Creates all of the Spaces and stores them in the 2D _spaces array
	 */
	private void _createSpaces() {
		_spaces = new Space[_COLUMNS][_ROWS];
		for (int col = 0; col < _COLUMNS; col++) {
			for (int row = 0; row < _ROWS; row++) {
				BoardCoordinate boardCoordinate = new BoardCoordinate(col, row);
				Space space = new Space(this, boardCoordinate);
				_spaces[col][row] = space;
			}
		}
	}

	/**
	 * 
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 *
	 */
	public class SpaceChangedEventData extends EventData {
		public int column, row;
		public PlayerID ownerPlayerID;

		public SpaceChangedEventData(int column, int row, PlayerID playerID) {
			this.column = column;
			this.row = row;
			this.ownerPlayerID = playerID;
		}
	}

}