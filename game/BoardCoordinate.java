package connect4.game;

import java.lang.Comparable;

/**
 * Is used to determine Coordinates on the Board
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public class BoardCoordinate implements Comparable<BoardCoordinate> {
	
	//The column and row coordinates of this
	private int _column, _row;

	/**
	 * Just creates a BoardCoordinate with the given column and row
	 * @param column The column
	 * @param row The row
	 */
	public BoardCoordinate(int column, int row) {
		_column = column;
		_row = row;
	}

	/**
	 * Gets the column
	 * @return Returns the column
	 */
	public int getColumn() {
		return _column;
	}

	/**
	 * Gets the row
	 * @return Returns the row
	 */
	public int getRow() {
		return _row;
	}

	/**
	 * Adds this BoardCoordinate and another.
	 * Formula: (this.column + addend.column, this.row + addend.row)
	 * @param addend The BoardCoordinate to add to this BoardCoordinate
	 * @return Returns the resulting BoardCoordinate
	 */
	public BoardCoordinate add(BoardCoordinate addend) {
		int newColumn = _column + addend.getColumn();
		int newRow = _row + addend.getRow();

		BoardCoordinate summedBoardCoordinate = new BoardCoordinate(newColumn, newRow);
		return summedBoardCoordinate;
	}

	/**
	 * Subtracts another from this BoardCoordinate.
	 * Formula: (this.column - subtrahend.column, this.row - subtrahend.row)
	 * @param subtrahend The BoardCoordinate to subtract from this BoardCoordinate
	 * @return Returns the resulting BoardCoordinate
	 */
	public BoardCoordinate subtract(BoardCoordinate subtrahend) {
		int newColumn = _column - subtrahend.getColumn();
		int newRow = _row - subtrahend.getRow();

		BoardCoordinate summedBoardCoordinate = new BoardCoordinate(newColumn, newRow);
		return summedBoardCoordinate;
	}

	/**
	 * Multiplies this BoardCoordinate by a scalar.
	 * Formula: (this.column * scalar, this.row * scalar)
	 * @param scalar The scalar to multiply this BoardCoordinate by
	 * @return Returns the resulting BoardCoordinate
	 */
	public BoardCoordinate scalarMultiply(int scalar) {
		int newColumn = _column * scalar;
		int newRow = _row * scalar;

		BoardCoordinate summedBoardCoordinate = new BoardCoordinate(newColumn, newRow);
		return summedBoardCoordinate;
	}

	/**
	 * Determines whether this BoardCoordinate is positive, meaning that its column is greater than 0 or, if column equals 0, its row is greater 0
	 * @return Returns true if this BoardCoordinate counts as "positive"
	 */
	public boolean isPositive() {
		if (_column < 0) { //If column < 0, always negative
			return false;
		}
		if (_column > 0) { //If column > 0, always positive
			return true;
		}
		//Okay, column is 0
		if (_row < 0) { //So, column = 0 and row < 0 is negative
			return false;
		}
		if (_row > 0) { //So, column = 0 and row > 0 is positive
			return true;
		}
		return false; //This only happens at (0,0)
	}

	@Override
	/**
	 * Returns a String of the format "(COLUMN,ROW)"
	 */
	public String toString() {
		return "(" + _column + "," + _row + ")";
	}
	
	@Override
	/**
	 * Determines whether two Objects are equal. No object is equal with a BoardCoordinate if it itself is not a BoardCoordinate, and both the columns and rows must match.
	 */
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (!(object instanceof BoardCoordinate)) {
			return false;
		}
		if (object == this) {
			return true;
		}
		
		BoardCoordinate otherBoardCoordinate = (BoardCoordinate) object;
		
		if (otherBoardCoordinate.getColumn() != _column) {
			return false;
		}
		if (otherBoardCoordinate.getRow() != _row) {
			return false;
		}
		
		return true;
	}

	@Override
	/**
	 * Compares this BoardCoordinate to another BoardCoordinate. Returns 0 if equal, 1 if this BoardCoordinate is greater than the other, and -1 if otherwise.
	 */
	public int compareTo(BoardCoordinate otherBoardCoordinate) {
		BoardCoordinate deltaCoordinate = subtract(otherBoardCoordinate);

		if (deltaCoordinate.equals(this)) {
			return 0;
		}
		
		if (deltaCoordinate.isPositive()) {
			return 1;
		}
		
		return -1;
	}

}