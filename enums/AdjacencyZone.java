package connect4.enums;

import connect4.game.BoardCoordinate;

/**
 * This enum holds data regarding all eight possible directions that Spaces can have as AdjacencyZones
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public enum AdjacencyZone {
	NONE(new BoardCoordinate(0,0)),
	NORTH(new BoardCoordinate(0,-1)),
	NORTHEAST(new BoardCoordinate(1,-1)),
	EAST(new BoardCoordinate(1,0)),
	SOUTHEAST(new BoardCoordinate(1,1)),
	SOUTH(new BoardCoordinate(0,1)),
	SOUTHWEST(new BoardCoordinate(-1,1)),
	WEST(new BoardCoordinate(-1,0)),
	NORTHWEST(new BoardCoordinate(-1,-1));

	//Just used to keep track of each Enum Value's BoardCoordinate
	private BoardCoordinate _offsetBoardCoordinate;

	/**
	 * Creates an AdjacencyZone with the given BoardCoordinate
	 * @param offsetBoardCoordinate The BoardCoordinate associated with the direction of this AdjacencyZone
	 */
	AdjacencyZone(BoardCoordinate offsetBoardCoordinate) {
		_offsetBoardCoordinate = offsetBoardCoordinate;
	}

	/**
	 * Gets the BoardCoordinate associated with the direction of this AdjacencyZone
	 * @return Returns the BoardCoordinate associated with the direction of this AdjacencyZone
	 */
	public BoardCoordinate getOffsetBoardCoordinate() {
		return _offsetBoardCoordinate;
	}

	/**
	 * Gets the opposite AdjacencyZone based on the direction of this AdjacencyZone's BoardCoordinate
	 * @return Returns the opposite AdjacencyZone based on the direction of this AdjacencyZone's BoardCoordinate
	 */
	public AdjacencyZone getOppositeAdjacencyZone() {
		BoardCoordinate reverseBoardCoordinate = _offsetBoardCoordinate.scalarMultiply(-1);
		AdjacencyZone oppositeAdjacencyZone = getAdjacencyZoneFromOffset(reverseBoardCoordinate);
		return oppositeAdjacencyZone;
	}

	/**
	 * Gets the AdjacencyZone that matches the given BoardCoordinate in direciton
	 * @param offsetBoardCoordinate The BoardCoordinate to match to an AdjacencyZone
	 * @return Returns the AdjacencyZone that matches the given BoardCoordinate in direciton
	 */
	public static AdjacencyZone getAdjacencyZoneFromOffset(BoardCoordinate offsetBoardCoordinate) {
		for (AdjacencyZone adjacencyZone : AdjacencyZone.values()) {
			if (adjacencyZone.getOffsetBoardCoordinate().equals(offsetBoardCoordinate)) {
				return adjacencyZone;
			}
		}
		return AdjacencyZone.NONE;
	}

	/**
	 * Gets the list of all AdjacencyZones that have positive BoardCoordinate directions, not including AdjacencyZone.NONE
	 * @return Returns the list of all AdjacencyZones that have positive BoardCoordinate directions, not including AdjacencyZone.NONE
	 */
	public static AdjacencyZone[] getPositiveAdjacencyZones() {
		AdjacencyZone[] allAdjacencyZones = AdjacencyZone.values();
		int arrayLength = allAdjacencyZones.length / 2;
		AdjacencyZone[] positiveAdjacencyZones = new AdjacencyZone[arrayLength];
		int currentIndex = 0;

		for (AdjacencyZone adjacencyZone : allAdjacencyZones) {
			BoardCoordinate offsetBoardCoordinate = adjacencyZone.getOffsetBoardCoordinate();
			if (offsetBoardCoordinate.isPositive()) {
				positiveAdjacencyZones[currentIndex] = adjacencyZone;
				currentIndex++;
			}
		}

		return positiveAdjacencyZones;
	}

	/**
	 * Gets the list of all AdjacencyZones that have negative BoardCoordinate directions, not including AdjacencyZone.NONE
	 * @return Returns the list of all AdjacencyZones that have negative BoardCoordinate directions, not including AdjacencyZone.NONE
	 */
	public static AdjacencyZone[] getNegativeAdjacencyZones() {
		AdjacencyZone[] allAdjacencyZones = AdjacencyZone.values();
		int arrayLength = allAdjacencyZones.length / 2;
		AdjacencyZone[] negativeAdjacencyZones = new AdjacencyZone[arrayLength];
		int currentIndex = 0;

		for (AdjacencyZone adjacencyZone : allAdjacencyZones) {
			BoardCoordinate offsetBoardCoordinate = adjacencyZone.getOffsetBoardCoordinate();
			if (!offsetBoardCoordinate.isPositive()) {
				negativeAdjacencyZones[currentIndex] = adjacencyZone;
				currentIndex++;
			}
		}

		return negativeAdjacencyZones;
	}
}