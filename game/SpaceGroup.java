package connect4.game;

import connect4.enums.*;
import java.util.ArrayList;

/**
 * This class is used to keep track of adjacent spaces in a given direction that all have the same owner PlayerID
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public class SpaceGroup {
	
	//Just some required data
	private AdjacencyZone _adjacencyZone; //Note, this should always be a positive adjacency zone
	private ArrayList<Space> _spaces;

	/**
	 * Creates a new SpaceGroup with the given AdjacencyZone as its direction and the given Space as its base, then continues to add Spaces in the direction of its AdjacencyZone until it no longer can successfully add Spaces
	 * @param adjacencyZone The AdjacencyZone representing the direction of this SpaceGroup
	 * @param baseSpace The Space that will be first in this SpaceGroup
	 */
	public SpaceGroup(AdjacencyZone adjacencyZone, Space baseSpace) {
		_adjacencyZone = adjacencyZone;
		_spaces = new ArrayList<Space>();
		_spaces.add(baseSpace);
		
		while (addNextPositiveSpace()) {
			//Just keep adding the next positive space until it won't work
		}
	}

	/**
	 * Gets the AdjacencyZone of this SpaceGroup
	 * @return Returns the AdjacencyZone of this SpaceGroup
	 */
	public AdjacencyZone getAdjacencyZone() {
		return _adjacencyZone;
	}

	/**
	 * Gets the list of Spaces in this SpaceGroup
	 * @return Returns the list of Spaces in this SpaceGroup
	 */
	public Space[] getSpaces() {
		return _spaces.toArray(new Space[0]);
	}
	
	/**
	 * Gets the number of Spaces in this SpaceGroup
	 * @return Returns the number of Spaces in this SpaceGroup
	 */
	public int getLength() {
		return _spaces.size();
	}

	/**
	 * Gets the PlayerID that is associated as the owner of all of the Spaces in this SpaceGroup
	 * @return Returns the PlayerID that is associated as the owner of all of the Spaces in this SpaceGroup, returns null if this SpaceGroup is empty
	 */
	public PlayerID getOwnerPlayerID() {
		if (_spaces.size() == 0) {
			return PlayerID.NONE;
		}
		return _spaces.get(0).getOwnerPlayerID();
	}

	/**
	 * Adds a new Space to this SpaceGroup, appending to it from the end by moving up one more in the direction of the associated AdjacencyZone
	 * @return Returns true if a Space was successfully added, false otherwise
	 */
	public boolean addNextPositiveSpace() {
		Space positiveNextSpace = getPositiveNextSpace();
		
		if (positiveNextSpace == null) { //If no next positive Space exists, return false
			return false;
		}
		if (_spaces.size() > 0 && !positiveNextSpace.getOwnerPlayerID().equals(getOwnerPlayerID())) { //If this SpaceGroup already has an owner and the next Space's owner does not match, return false
			return false;
		}
		
		_spaces.add(positiveNextSpace);
		
		return true;
	}

	/**
	 * Adds a new Space to this SpaceGroup, appending to it from the end by moving up one more in the opposite direction of the associated AdjacencyZone
	 * @return Returns true if a Space was successfully added, false otherwise
	 */
	public boolean addNextNegativeSpace() {
		Space negativeNextSpace = getNegativeNextSpace();
		
		if (negativeNextSpace == null) { //If no next negative Space exists, return false
			return false;
		}
		if (_spaces.size() > 0 && !negativeNextSpace.getOwnerPlayerID().equals(getOwnerPlayerID())) { //If this SpaceGroup already has an owner and the next Space's owner does not match, return false
			return false;
		}
		
		_spaces.add(0, negativeNextSpace);
		
		return true;
	}
	
	/**
	 * Gets the next Space when following the associated AdjacencyZone
	 * @return Returns the next Space when following the associated AdjacencyZone
	 */
	public Space getPositiveNextSpace() {
		Space lastSpace = _spaces.get(_spaces.size() - 1); //Will check the positive direction on this

		//The next space if the space group continued in the positive direction
		Space positiveSpace = lastSpace.getAdjacentSpace(_adjacencyZone);
		
		return positiveSpace;
	}
	
	/**
	 * Gets the next Space when following opposite the associated AdjacencyZone
	 * @return Returns the next Space when following opposite the associated AdjacencyZone
	 */
	public Space getNegativeNextSpace() {
		AdjacencyZone negativeAdjacencyZone = _adjacencyZone.getOppositeAdjacencyZone();
		Space firstSpace = _spaces.get(0); //Will check the negative direction on this
		//The next space if the space group continued in the negative direction
		Space negativeSpace = firstSpace.getAdjacentSpace(negativeAdjacencyZone);
		
		return negativeSpace;
	}

	/**
	 * Determines whether this SpaceGroup is locked, i.e. both ends in the given AdjacencyZone direction are blocked
	 * @return Returns true if this SpaceGroup is locked, i.e. both ends in the given AdjacencyZone direction are blocked, false otherwise
	 */
	public boolean isLocked() {
		Space positiveSpace = getPositiveNextSpace();
		Space negativeSpace = getNegativeNextSpace();

		//If both ends are blocked by the other player or the wall
		if ((positiveSpace == null || !positiveSpace.isEmpty()) && 
			(negativeSpace == null || !negativeSpace.isEmpty())) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * Returns a String representing the SpaceGroup that is just the concatenation of all of the Space's BoardCoordinate Strings in order
	 */
	public String toString() {
		String string = "";
		for (int i = 0; i < _spaces.size(); i++) {
			if (i > 0) {
				string += ",";
			}
			string += _spaces.get(i).getBoardCoordinate().toString();
		}
		return string;
	}
}