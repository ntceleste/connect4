package connect4.enums;

/**
 * This enum holds data regarding the two PlayerIDs
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public enum PlayerID {
	NONE("None"),
	PLAYER1("Player 1"),
	PLAYER2("Player 2");

	//The String representation of each PlayerID
	private String _string;

	/**
	 * Creats a new PlayerID with the given String representation
	 * @param string The String representation of this PlayerID
	 */
	PlayerID(String string) {
		_string = string;
	}

	@Override
	/**
	 * Returns the String representation of this PlayerID
	 */
	public String toString() {
		return _string;
	}

	/**
	 * Gets the opposite PlayerID
	 * @return Returns the opposite PlayerID
	 */
	public PlayerID getOppositePlayerID() {
		if (_string.equals("None")) {
			return PlayerID.NONE;
		}

		if (_string.equals("Player 1")) {
			return PlayerID.PLAYER2;
		}
		
		return PlayerID.PLAYER1;
	}
}