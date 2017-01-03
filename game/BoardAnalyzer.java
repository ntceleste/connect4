package connect4.game;

import connect4.enums.*;
import java.util.*;

/**
 * This class is used to analyze a Board and find SpaceGroups
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 *
 */
public class BoardAnalyzer {

	/**
	 * Finds all space groups of size minimumSize and larger within the Board board.
	 * @param board The Board to analyze
	 * @param minimumSize The minimumSize of the SpaceGroups to return
	 * @param includeLockedGroups If true, locked groups will be included.
	 * @return Returns an array of all the SpaceGroups that were found in the Board and fit the criteria
	 */
	public static SpaceGroup[] getSpaceGroups(Board board, int minimumSize, boolean includeLockedGroups) {
		ArrayList<SpaceGroup> spaceGroups = new ArrayList<SpaceGroup>();

		SpaceGroup[] spaceGroupsIncludingSpace = new SpaceGroup[5]; //More efficient to just use one array over and over even if we never use all of the spaces.
		for (int column = 0; column < board.getNumberOfColumns(); column++) {
			for (int row = 0; row < board.getNumberOfRows(); row++) {
				Space space = board.getSpace(column, row);

				if (space.isEmpty()) { //If it is empty, this won't belong to a SpaceGroup anyway
					continue;
				}

				_getSpaceGroupsIncludingSpace(board, space, spaceGroupsIncludingSpace); //This will actually get the space groups
				for (int i = 0; i < spaceGroupsIncludingSpace.length; i++) {
					SpaceGroup spaceGroup = spaceGroupsIncludingSpace[i];

					if (spaceGroup == null) { //At this point, we have reached the end of the existing groups
						break;
					}

					if (spaceGroup.getLength() < minimumSize) { //Too small
						continue;
					}
					
					if (includeLockedGroups || !spaceGroup.isLocked()) { //Ignores locked groups if includeLockedGroups is false
						spaceGroups.add(spaceGroup);
					}

					spaceGroupsIncludingSpace[i] = null; //Clear the spaceGroupsIncludingSpace array as we go
				}
			}
		}

		return spaceGroups.toArray(new SpaceGroup[spaceGroups.size()]);
	}

	/**
	 * Finds all space groups of size minimumSize and larger within the Board board.
	 * @param board The Board to analyze
	 * @param minimumSize The minimumSize of the SpaceGroups to return
	 * @return Returns an array of all the SpaceGroups that were found in the Board and fit the criteria, assumes not to include locked groups
	 */
	public static SpaceGroup[] getSpaceGroups(Board board, int minimumSize) {
		return getSpaceGroups(board, minimumSize, false);
	}

	/**
	 * Finds all space groups of size minimumSize and larger within the Board board.
	 * @param board The Board to analyze
	 * @return Returns an array of all the SpaceGroups that were found in the Board and fit the criteria, assumes minimum size of 2 and not to include locked groups
	 */
	public static SpaceGroup[] getSpaceGroups(Board board) {
		return getSpaceGroups(board, 2);
	}

	/**
	 * Finds all SpaceGroups including this space in any positive direction
	 * @param board The Board to find the SpaceGroups in
	 * @param space The Space to use to find the SpaceGroups
	 * @param spaceGroupsIncludingSpace The array to store the SpaceGroups in
	 */
	private static void _getSpaceGroupsIncludingSpace(Board board, Space space, SpaceGroup[] spaceGroupsIncludingSpace) {
		int currentIndex = 0;
		
		/*
		 * The use of only positive AdjacencyZones helps to make this more efficient and less wasteful.
		 * No point in going in all directions as this would result in duplicate SpaceGroups.
		 */
		for (AdjacencyZone adjacencyZone : AdjacencyZone.getPositiveAdjacencyZones()) {
			Space previousSpace = space.getAdjacentSpace(adjacencyZone.getOppositeAdjacencyZone());
			if (previousSpace != null && previousSpace.getOwnerPlayerID().equals(space.getOwnerPlayerID())) {
				/*
				 * So this part may be somewhat confusing.
				 * The general idea is that if a space in a negative direction also matches this Space's owner then
				 * this Space and this previousSpace already exist in a SpaceGroup with a positive AdjacencyZone direction
				 * in which the previousSpace comes first.
				 * So, therefore, it is a waste of time and memory to create another SpaceGroup that essentially already exists.
				 * Plus, if we create a SpaceGroup in one direction but start somewhere in the middle, this SpaceGroup will only include
				 * less information than the earlier one.
				 */
				continue;
			}
			
			SpaceGroup spaceGroup = new SpaceGroup(adjacencyZone, space);
			
			spaceGroupsIncludingSpace[currentIndex] = spaceGroup;
			currentIndex++;
		}
	}
}