package connect4.players;

import connect4.enums.*;

/**
 * This abstract class is to be used as a base for both the HumanPlayer and ComputerPlayer
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public abstract class Player {

	//A bunch of protected stuff so each Player object can function properly
	protected PlayerID _playerID;

	/**
	 * Creates a new Player to be associated with the given PlayerID
	 * @param playerID The PlayerID to be associated with this new Player
	 */
	public Player(PlayerID playerID) {
		_playerID = playerID;
	}

	/**
	 * Gets the PlayerID associated with this Player
	 * @return Returns the PlayerID associated with this Player
	 */
	public PlayerID getPlayerID() {
		return _playerID;
	}

	/**
	 * To be implemented by each class that extends from this so as to take its turn
	 */
	public abstract void play();
	
}