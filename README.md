# Connect4
File Descriptions:
	enums
		AdjacencyZone
			An enum describing all eight directions, plus the neutral NONE, that tokens can have in relation to one another.
		PlayerID
			An enum describing Player1 and Player2 to help distinguish them with less hard coding.
	events
		EventData
			Literally just an empty class to inherit from
		EventSource
			An EventSource to be used so as to hook up handlers to events
		IEventListener
			An interface to apply to other classes so as to allow them to be added as listeners to EventSources
	game
		Board
			This holds all of the Space objects (see below) in a 2D-array representing the grid of spaces
		BoardAnalyzer
			This static class allows us to find all SpaceGroup objects (see below)
		BoardCoordinate
			A simple class that holds an integer for the column and row on the board, and also includes several methods for basic arithmetic operations and a method to determine whether the BoardCoordinate is "positive" or not.
		GameState
			This class holds a lot of data, including our Board and our GhostBoard (used by the ComputerPlayer objects [see below]), our two Players, and just the overall state of the game.
		Space
			This class is our node for our graph. It keeps track of its BoardCoordinate, which player (if any) owns it, and allows us to find other spaces in any of our eight directions.
		SpaceGroup
			This class is absolutely critical to our game. This is the holder for groups of Spaces that are adjacent and owned by the same player. We use this to determine whether a player has won and we use it a LOT for our ComputerPlayer (see below).
	guis
		Connect4Gui
			This class manages the entire graphical user interface
	players
		ComputerPlayer
			This is our AI. This class uses a minimax algorithm to decide how it should play each move, and uses the SpaceGroup class extensively to make its decisions. This may very well be the most complicated class in our project, or at the very least it is second to BoardAnalyzer.
		HumanPlayer
			This class exists almost exclusively to distinguish between a ComputerPlayer AI and an actual human.
		Player
			This is basically just an abstract class for both HumanPlayer and ComputerPlayer to inherit from, though it does have some minimal functionality.

Major Data Structures:
	Buttons array
		We used an array to keep track of the seven buttons along the top of our GUI for the human player to choose where to drop their token.
	Spaces grid
		A 2D-array held in Board to represent the hold between spaces.
	Space
		This class is the node of our graph

Sample Output:
	Ideally there is no output for this program. The gameplay is our output, I suppose. Also, our game outputs the time complexity of the AI into its own TextField at the bottom of the frame.
