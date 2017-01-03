package connect4.guis;

import connect4.enums.*;
import connect4.events.*;
import connect4.game.*;
import connect4.players.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * This class manages the entire user interface for the game
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public class Connect4Gui extends JFrame implements ActionListener {

	private static final long serialVersionUID = 700157330494090364L;

	/** stores the title of the window */
	private final String _TITLE = "Connect4";

	/** stores the initial dimensions of the window */
	private final int _WIDTH = 700, _HEIGHT = 700;

	/** stores the default */
	private final Color _BUTTON_COLOR = Color.WHITE;

	/** stores the color of the background */
	private final Color _BACKGROUND_COLOR = Color.BLUE;

	/** stores the game state */
	private GameState _gameState;

	/** stores the color for each player */
	private Map<PlayerID, Color> _playerColors;

	/** stores the JPanel that contains all the boxes and buttons */
	private JPanel _boardPanels;

	/** stores the JButton Array that makes up the board */
	private JButton[] _buttonArray;

	/** stores the Box Array that makes up the board */
	private Box[][] _boxArray;

	/** stores the Text Area that is used for the output of the AI run-time analysis */
	private JTextArea _textArea;

	/** stores the reference to the SpaceChangedManager */
	private SpaceChangedManager _spaceChangedManager;

	/** stores the reference to the TurnChangedManager */
	private TurnChangedManager _turnChangedManager;

	/** stores the reference to the GameOverManager */
	private GameOverManager _gameOverManager;

	/**
	 * Creates a new GUI for the game
	 * @param gameState The GameState for the GUI to display
	 */
	public Connect4Gui(GameState gameState) {
		_gameState = gameState;
		
		_spaceChangedManager = new SpaceChangedManager();
		_gameState.getBoard().spaceChanged.addListener(_spaceChangedManager);
		
		_turnChangedManager = new TurnChangedManager();
		_gameState.currentPlayerChanged.addListener(_turnChangedManager, true);
		
		_gameOverManager = new GameOverManager();
		_gameState.gameEnded.addListener(_gameOverManager);

		_buttonArray = new JButton[_gameState.getBoard().getNumberOfColumns()];
		_boxArray = new Box[_gameState.getBoard().getNumberOfColumns()][_gameState.getBoard().getNumberOfRows()];

		_createPlayerColors();
		_setLookAndFeel();
		_createBoard();
		_createButtons();
		_createBoxes();
		_createTextArea();
		_customizeJFrame();
	}

	@Override
	/**
	 * Listens for the buttons to be clicked and then attempts to drop a token in the selected column. This method will display a message if the user is unable to drop a token in that column.
	 */
	public void actionPerformed(ActionEvent e) {
		String message = "";

		boolean isCurrentPlayerHuman = _gameState.getCurrentPlayer() instanceof HumanPlayer;
		if (isCurrentPlayerHuman) {
			JButton eventSource = (JButton) e.getSource();
			int pressedButtonX = _getPressedButtonX(eventSource);
			boolean successfulDrop = _gameState.getBoard().dropToken(pressedButtonX);

			if (successfulDrop) {
				_gameState.goToNextPlayer();
			} else {
				message = "You selected a full column. Please select a different column";
			}
		} else {
			message = "Be patient, the computer is busy exploiting your weak human mind";
		}

		if (!message.equals("")) {
			_showMessage(message);
		}
	}

	/**
	 * Creates the two colors for each Player
	 */
	private void _createPlayerColors() {
		_playerColors = new HashMap<PlayerID, Color>();
		_playerColors.put(PlayerID.PLAYER1, Color.RED);
		_playerColors.put(PlayerID.PLAYER2, Color.YELLOW);
	}

	/**
	 * Sets the look and feel. We did not like having this try/catch in our constructor.
	 */
	private void _setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
	}

	/**
	 * Creates the board grid JPanel
	 */
	private void _createBoard() {
		_boardPanels = new JPanel(new GridLayout(_gameState.getBoard().getNumberOfRows() + 1,
				_gameState.getBoard().getNumberOfColumns()));
		getContentPane().add(_boardPanels, "Center");
	}

	/**
	 * Creates all of the buttons for the user to click
	 */
	private void _createButtons() {
		for (int panelIndexX = 0; panelIndexX < _gameState.getBoard().getNumberOfColumns(); panelIndexX++) {
			JButton button = new JButton();
			button.addActionListener(this);
			button.setBackground(_BUTTON_COLOR);

			_boardPanels.add(button);
			_buttonArray[panelIndexX] = button;
		}
	}

	/**
	 * Creates all of the boxes to represent each Space in the game
	 */
	private void _createBoxes() {
		Board board = _gameState.getBoard();
		for (int panelIndexY = 0; panelIndexY < board.getNumberOfRows(); panelIndexY++) {
			for (int panelIndexX = 0; panelIndexX < board.getNumberOfColumns(); panelIndexX++) {
				Box box = new Box(0);
				box.setOpaque(true);
				box.setBackground(_BACKGROUND_COLOR);
				box.setBorder(BorderFactory.createLineBorder(Color.black));

				_boardPanels.add(box);
				_boxArray[panelIndexX][panelIndexY] = box;
			}
		}
	}

	/**
	 * Creates the text area used to display the output of the AI run-time analysis
	 */
	private void _createTextArea() {
		_textArea = new JTextArea();
		_textArea.setRows(3);
		_textArea.setLineWrap(true);
		_textArea.setWrapStyleWord(true);
		_textArea.setMargin(new Insets(10, 10, 10, 10));
		_textArea.setFont(new Font("Arial", Font.PLAIN, 18));
		getContentPane().add(_textArea, BorderLayout.SOUTH);
	}

	/**
	 * Sets up the JFrame used for this GUI
	 */
	private void _customizeJFrame() {
		setSize(_WIDTH, _HEIGHT);
		setTitle(_TITLE);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Used to show a message in a popup to the user
	 * @param message The message to be displayed
	 */
	private void _showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	/**
	 * Sets the text of the text area
	 * @param message The text for the text area to show
	 */
	private void _setText(String message) {
		_textArea.setText(message);
	}

	/**
	 * Sets the title of the JFrame, appending the constant title at the end
	 * @param message The message to be displayed in the title
	 */
	private void _setTitle(String message) {
		if (message == null || message.length() == 0) {
			setTitle(_TITLE);
		}
		setTitle(message + " | " + _TITLE);
	}

	/**
	 * This is used to get the column number that a certain button represents
	 * @param pressedButton The button in question
	 * @return Returns the column that the button represents
	 */
	private int _getPressedButtonX(JButton pressedButton) {
		for (int i = 0; i < _buttonArray.length; i++) {
			if (_buttonArray[i].equals(pressedButton)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * This class is used to manage what happens when the spaceChanged event is fired on the Board
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 */
	private class SpaceChangedManager implements IEventListener<Board.SpaceChangedEventData> {
		@Override
		public void handleNotification(Board.SpaceChangedEventData eventData) {
			Box box = _boxArray[eventData.column][eventData.row];
			Color playerColor = _playerColors.get(eventData.ownerPlayerID);
			box.setBackground(playerColor);
		}
	}

	/**
	 * This class is used to manage what happens when the turn is changed in the GameState
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 */
	private class TurnChangedManager implements IEventListener<GameState.PlayerChangedEventData> {
		@Override
		public void handleNotification(GameState.PlayerChangedEventData data) {
			PlayerID previousPlayerID = data.newPlayerID.getOppositePlayerID();
			Player previousPlayer = _gameState.getPlayer(previousPlayerID);
			
			if (previousPlayer instanceof ComputerPlayer) {
				ComputerPlayer computerPlayer = (ComputerPlayer)previousPlayer;
				long millisecondsElapsed = computerPlayer.getLastTurnDuration();
				int numberOfTurns = computerPlayer.getNumberOfTurnsAnalyzed();
				double timeComplexity = Math.log(numberOfTurns) / Math.log(_gameState.getBoard().getNumberOfColumns());
				
				String message = "Your opponent took " + millisecondsElapsed
						+ " milliseconds to make their turn. " + "They looked at " + numberOfTurns
						+ " possible turns. The time complexity for this turn was O(N^" + timeComplexity + ")";
				_setText(message);
			}
			
			String currentPlayerString = data.newPlayerID.toString();
			
			_setTitle(currentPlayerString);

			Player player = _gameState.getPlayer(data.newPlayerID);
			if (player instanceof HumanPlayer) {
				_showMessage("It is " + currentPlayerString + "'s turn");
			}
		}
	}
	
	/**
	 * This class is used to manage what happens when the gameEnded event is fired on the GameState
	 * @author Nate Celeste NTC14, Noah Crowley NWC17
	 */
	private class GameOverManager implements IEventListener<GameState.GameOverEventData> {
		@Override
		public void handleNotification(GameState.GameOverEventData data) {
			PlayerID winningPlayer = data.winnerPlayerID;
			
			String message = "Congratulations " + winningPlayer.toString() + "! You have won!";
			_showMessage(message);
			_setTitle(message);
		}
	}
}