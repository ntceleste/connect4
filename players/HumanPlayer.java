package connect4.players;

import connect4.enums.*;

/**
 * This is essentially a dummy class meant only to distinguish between the ComputerPlayer and the HumanPlayer
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public class HumanPlayer extends Player {

	public HumanPlayer(PlayerID playerID) {
		super(playerID);
	}

    @Override
    public void play() {
    }
}
