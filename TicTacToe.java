import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements an AI that plays a game of Tic-Tac-Toe with
 * a human player.
 * @author Vecheka Chhourn
 * @version 1.0
 */
public class TicTacToe {
	
	/** Board's height.*/
	private static final int HEIGHT = 3;
	/** Board's width.*/
	private static final int WIDTH = 3;
	/** Number representing nothing is found.*/
	private static final int NOTHING = -1;
	/** Number representing two letters are found.*/
	private static final int TWO = 0;
	/** Number representing a block.*/
	private static final int BLOCK = 1;
	/** Number representing a win for bot.*/
	private static final int BOT_WIN = 2;
	/** Number representing a win for user.*/
	private static final int USER_WIN = -2;
	/** Count of game's moves.*/
	private static int myMoveCount;
	/** Bot's Score.*/
	private static int myBotScore;
	/** User's score.*/
	private static int myUserScore;
	/** Bot's letter.*/
	private String myBotLetter;
	/** User's letter.*/
	private String myUserLetter;
	/** Game board.*/
	private String[][] myBoard;
	/** List of available spaces on the board.*/
	private List<Point> myAvaibleSpaces;
	/** True if A.I wins.*/
	private boolean botWins;
	/** True if user wins.*/
	private boolean userWins;
	
	
	/**
	 * Constructor that takes letters representing AI player and human player,
	 * and initialize game board with number 1-9 representing the available spaces
	 * @param theBotLetter letter representing AI player
	 * @param theUserLetter letter representing human player
	 */
	public TicTacToe(final String theBotLetter, final String theUserLetter) {
		myBotLetter = theBotLetter;
		myUserLetter = theUserLetter;
		myBoard = new String[WIDTH][HEIGHT];
		initializeBoard();
	}
	
	/**
	 * Initialize game board with number 1-9 representing the available spaces on the board.
	 */
	private void initializeBoard() {
		int num = 1;
		for (int r = 0; r < WIDTH; r++) {
			for (int c = 0; c < HEIGHT; c++) {
				myBoard[r][c] = num + "";
				num++;
			}
			
		}
	}
	
	/**
	 * Place the letter on the board.
	 * @param theNumber number representing the space on the board
	 */
	public void place(final String theNumber) {
		int row = 0, col = 0;
		for (int r = 0; r < WIDTH; r++) {
			for (int c = 0; c < HEIGHT; c++) {
				if (myBoard[r][c].equalsIgnoreCase(theNumber)) {
					myBoard[r][c] = myUserLetter;
					row =  r;
					col = c;
					break;
				}
			}
		}
		myMoveCount++;
		
		if (myMoveCount == WIDTH * HEIGHT
				&& (!userWins && !botWins)) {
			// draw
			userWins = true;
			botWins = true;
			return;
		}
		
		updateAvailableSpace();
		getGameState();
		if (userWins) {
//			reset();
			return;
		}
		final int mid = WIDTH / 2;
		// if there is only one move, there are four conditions to follow
		// 1. if it is the edge, pick the middle space
		// 2. if it is the middle row and side columns, pick the row above
		// 3. if it is the last row and middle column, pick the first row and middle column
		// 4. if it is the middle row and middle column, pick the first row and first column 
		if (myMoveCount == 1) {
			if (isEdge(row, col)) {
				myBoard[mid][mid] = myBotLetter;
			} else if ((row == mid && col == mid)
					|| (row == 0 && col == mid)) {
				myBoard[0][0] = myBotLetter;
			} else if (row == mid && (col == 0 || col == myBoard.length - 1)) {
				myBoard[row - 1][col] = myBotLetter;
			} else if (row == WIDTH - 1 && col == mid) {
				myBoard[0][mid] = myBotLetter; 
			}
			myMoveCount++;
		} else if (myMoveCount <= WIDTH * HEIGHT - 1){
			// find the best move
//			System.out.println(space);
			final Point p = myAvaibleSpaces.get(getBestMove());
			myBoard[p.x][p.y] = myBotLetter;
			myMoveCount++;
		}
		
//		if (botWins) {
//			reset();
//		}
	}
	
	/**
	 * Check if the user has won the game or it is a draw.
	 */
	private void getGameState() {
		int row = 0, col = 0;
		// get row data
		while (row < WIDTH) {
			if (getResult(getRow(row, myBoard.clone())) == USER_WIN) {
				userWins = true;
				return;
			} 
			row++;
		}
		// get column data
		while (col < WIDTH) {
			if (getResult(getCol(col, myBoard.clone())) == USER_WIN) {
				userWins = true;
				return;
			}
			col++;
		}
		
		// get diagonal data
		row = 0; col = 0;
		if (getResult(getDiagonal(row, col, myBoard.clone())) == USER_WIN) {
			userWins = true;
			return;
		} else if (getResult(getDiagonal(row, WIDTH - 1, myBoard.clone())) == USER_WIN) {
			userWins = true;
			return;
		}
	}

	/** 
	 * Reset game board, and move counts.
	 */
	public void reset() {
		initializeBoard();
		myMoveCount = 0;
		botWins = false;
		userWins = false;	
	}
	
	/**
	 * Reset score lines.
	 */
	public void resetScore() {
		myBotScore = myUserScore = 0;
	}

	/**
	 * Get the best possible move according to the current state on the board.
	 * @return index of the space's point found in myAvailabeSpace list
	 */
	private int getBestMove() {
		final String[][] tempBoard = myBoard.clone();
		int index = 0;
		List<Integer> possibleMoves = new ArrayList<>();
		while (index < myAvaibleSpaces.size()) {
			final Point p = myAvaibleSpaces.get(index);
			final String temp = tempBoard[p.x][p.y];
			tempBoard[p.x][p.y] = myBotLetter;
			possibleMoves.add(getCurrentState(p.x, p.y, tempBoard));
			tempBoard[p.x][p.y] = temp;
			index++;
		}
		if (findMax(possibleMoves) == BOT_WIN) botWins = true;
		return possibleMoves.indexOf(findMax(possibleMoves));
	}
	
	/**
	 * Get the current state of the board with the a move from A.I at 
	 * a given coordinate on the board.
	 * @param theRow row on the board
	 * @param theCol column on the board
	 * @param theBoard copy of the original game board
	 * @return -1 if nothing is found, 0 if it has at least letters
	 * 			1 if it is a block, and 2 if it is a win
	 */
	private Integer getCurrentState(final int theRow, final int theCol, 
			final String[][] theBoard) {
		List<Integer> result = new ArrayList<>();
		List<String> rowList = null;
		List<String> colList = null;
		List<String> diagonalList = null;
		if (isEdge(theRow, theCol)) {
			// check the row, column, and diagonal
			diagonalList = getDiagonal(theRow, theCol, theBoard);
		} 
		rowList = getRow(theRow, theBoard);
		colList = getCol(theCol, theBoard);
		result.add(getResult(rowList));
		result.add(getResult(colList));
		if (diagonalList != null) {
			result.add(getResult(diagonalList));
		}
		
		
		return findMax(result);
	}
	

	/**
	 * Return the current state of the board using one of the number representer.
	 * @param theList list of data on the board
	 * @return -1 if nothing is found, 0 if it has at least letters
	 * 		    1 if it is a block, and 2 if it is a win
	 */
	private Integer getResult(final List<String> theList) {
		int countBot = 0;
		int countUser = 0;
		for (final String letter: theList) {
			if (letter.equalsIgnoreCase(myBotLetter)) countBot++;
			else if (letter.equalsIgnoreCase(myUserLetter)) countUser++;
		}
		int result = NOTHING;
		if (countUser == WIDTH - 1 && countBot == 1) {
			result = BLOCK;
		} else if (countBot == WIDTH - 1) {
			result = TWO;
		} else if (countBot == WIDTH) {
			result = BOT_WIN;
		} else if (countUser == WIDTH) {
			result = USER_WIN;
		}
		return result;
	}
	
	/**
	 * Find the max number in the list.
	 * @param theList list to be used
	 * @return max number
	 */
	private int findMax(final List<Integer> theList) {
		int max = 0;
		for(final Integer num: theList) {
			if (num > max) max = num;
		}
		return max;
	}
	
	/**
	 * Get diagonal data from the board
	 * @param theRow row on the board
	 * @param theCol column on the board
	 * @param theBoard copy of the original game board
	 * @return a list of column data
	 */
	private List<String> getDiagonal(final int theRow, final int theCol, 
			final String[][] theBoard) {
		List<String> list = new ArrayList<>();
		if ((theRow == 0 && theCol == 0) 
				|| (theRow == WIDTH - 1 && theCol == WIDTH - 1)) {
			int c = 0;
			for (int r = 0; r < theBoard.length; r++) {
				list.add(theBoard[r][c]);
				c++;
			}
		} else {
			int c = theBoard.length -  1;
			for (int r = 0; r < theBoard.length; r++) {
				list.add(theBoard[r][c]);
				c--;
			}
			
		}
		return list;
	}
	
	/**
	 * Get column data from the board
	 * @param theCol column on the board
	 * @param theBoard copy of the original game board
	 * @return a list of column data
	 */
	private List<String> getCol(final int theCol, final String[][] theBoard) {
		List<String> list = new ArrayList<>();
		for (int r = 0; r < theBoard.length; r++) {
			list.add(theBoard[r][theCol]);
		}
		return list;
	}

	/**
	 * Get row data from the board
	 * @param theRow row on the board
	 * @param theBoard copy of the original game board
	 * @return a list of row data
	 */
	private List<String> getRow(final int theRow, final String[][] theBoard) {
		List<String> list = new ArrayList<>();
		for (int c = 0; c < theBoard.length; c++) {
			list.add(theBoard[theRow][c]);
		}
		return list;
	}

	/**
	 * Check if it is the edge of the board.
	 * @param theRow row of the board
	 * @param theCol column of the board
	 * @return true if it is the edge
	 */
	private boolean isEdge(final int theRow, final int theCol) {
		return (theRow == 0 || theRow == myBoard.length - 1) 
				&& (theCol == 0 || theCol == myBoard[0].length - 1);
	}

	/**
	 * Update the list of the available spaces on the board.
	 */
	private void updateAvailableSpace() {
		myAvaibleSpaces = new ArrayList<>();
		for (int r = 0; r < WIDTH; r++) {
			for (int c = 0;  c < HEIGHT; c++) {
				if (!myBoard[r][c].equalsIgnoreCase(myBotLetter)
						&& !myBoard[r][c].equalsIgnoreCase(myUserLetter)) {
					myAvaibleSpaces.add(new Point(r, c));
				}
			}
		}
		
	}
	
	/** 
	 * Getter for botWins
	 * @return true if bot wins
	 */
	public boolean getBotWin() {
		return botWins;
	}
	
	/** 
	 * Getter for botWins
	 * @return true if bot wins
	 */
	public boolean getUserWin() {
		return userWins;
	}
	
	/**
	 * Show message of game stats.
	 */
	public String showResult() {
		String result = "Game Stats: ";
		if (botWins && userWins) {
			result += "It is a draw!";
		} else if (botWins) {
			result += "I, the bot wins!";
			myBotScore++;
		} else if (userWins) {
			result += "You win!";
			myUserScore++;
		} else if (myMoveCount < WIDTH * HEIGHT) {
			result += "Game's still in progess...";
		}
		return result + "\nScore: Bot: " + myBotScore + " You: " + myUserScore;
	}
	
	/** 
	 * String representation of the game board.
	 * @return game board in string's form
	 */
	public String toString() {
		final StringBuilder board = new StringBuilder();
		for (int r = 0; r < myBoard.length; r++) {
			board.append("| ");
			for (int c = 0; c < myBoard[0].length; c++) {
				board.append(myBoard[r][c] + "  |  ");
			}

			board.append("\n---+---+---+\n");
		}
		return board.toString();
	}
}
