package connect4.game;

import connect4.enums.*;

/**
 * This class is the primary Node and Graph piece of the entire game structure
 * This class will contain a BoardCoordinate and a PlayerID to keep track of its own state
 * It will also keep a reference to its parent Board so as to find neighboring Space objects
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public class Space {

	//Variables used to store the information needed to fulfill its functions
	private Board _board;
	private PlayerID _ownerPlayerID;
	private BoardCoordinate _boardCoordinate;
	
	/**
	 * Creates a Space at the given BoardCoordinate with an ownerPlayerID of PlayerID.NONE
	 * @param board The Board this Space will be associated with
	 * @param boardCoordinate The BoardCoordinate representing the position of this Space on its parent Board
	 */
	public Space(Board board, BoardCoordinate boardCoordinate) {
		_board = board;
		_boardCoordinate = boardCoordinate;
		_ownerPlayerID = PlayerID.NONE;
	}
	
	/**
	 * Gets the BoardCoordinate of this Space
	 * @return Returns the BoardCoordinate of this Space
	 */
	public BoardCoordinate getBoardCoordinate() {
		return _boardCoordinate;
	}
	
	/**
	 * Gets the PlayerID of the Player who owns this Space, PlayerID.NONE if no one owns it
	 * @return Returns the PlayerID of the Player who owns this Space, PlayerID.NONE if no one owns it
	 */
	public PlayerID getOwnerPlayerID() {
		return _ownerPlayerID;
	}
	
	/**
	 * Sets the PlayerID to represent the owner of this Space
	 * @param playerID The PlayerID of the new owner of this Space. Passing in PlayerID.NONE will make this space empty.
	 */
	public void setOwnerPlayerID(PlayerID playerID) {
		_ownerPlayerID = playerID;
	}
	
	/**
	 * Determines whether this Space is empty, i.e. whether this Space's owner is PlayerID.NONE
	 * @return Returns true if the owner PlayerID of this Space is PlayerID.NONE, false otherwise
	 */
	public boolean isEmpty() {
		return _ownerPlayerID == PlayerID.NONE;
	}

	/**
	 * Gets the BoardCoordinate in the given direction of the provided AdjacencyZone.
	 * @param adjacencyZone The AdjacencyZone used to determine which direction to get the adjacent BoardCoordinate
	 * @return Returns a BoardCoordinate that is offset by the AdjacencyZone's direction
	 */
	public BoardCoordinate getAdjacentBoardCoordinate(AdjacencyZone adjacencyZone) {
		return _boardCoordinate.add(adjacencyZone.getOffsetBoardCoordinate());
	}

	/**
	 * Gets the Space adjacent to this Space in the given direction of the provided AdjacencyZone
	 * @param adjacencyZone The AdjacencyZone used to determine which direction to get the adjacent Space
	 * @return Returns the adjacent Space in the direction of the AdjacencyZone, returns null if no Space exists in that direction
	 */
	public Space getAdjacentSpace(AdjacencyZone adjacencyZone) {
		if (adjacencyZone == AdjacencyZone.NONE) {
			return null;
		}

		BoardCoordinate adjacentSpaceBoardCoordinate = getAdjacentBoardCoordinate(adjacencyZone);
		int adjacentColumn = adjacentSpaceBoardCoordinate.getColumn();
		int adjacentRow = adjacentSpaceBoardCoordinate.getRow();

		if (adjacentColumn < 0 || adjacentColumn >= _board.getNumberOfColumns() || adjacentRow < 0 || adjacentRow >= _board.getNumberOfRows()) {
			return null;
		}

		Space adjacentSpace = _board.getSpace(adjacentColumn, adjacentRow);
		return adjacentSpace;
	}
}