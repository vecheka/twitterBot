
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


import twitter4j.*;


/**
 * A class that implements a game of tic-tac-toe via tweeting.
 * @author Vecheka Chhourn
 * @version 1.0
 */
public class vechekaBot implements Observer {
	 //access the twitter API using your twitter4j.properties file
    private final Twitter twitter = TwitterFactory.getSingleton();

    /** Space on the board as numbers 1-9.*/
    private static final String[] SPACE_NUMBER = {"1", "2", "3" , "4"
    		, "5", "6", "7", "8", "9"};
    /** Yes-Response List.*/
	private static final String[] YES_LIST = {"yes", "ok",
			"alright", "let's go", "okay", "yeah"};
	/** No-Response List.*/
	private static final String[] NO_LIST = {"No", "Nah", "I'm good"
			,"Im good"};
    
	/** Default Bot's letter.*/
	private static String myBotLetter = "O";
	/** DefaultUser's letter.*/
	private static String myUserLetter = "X";
	/** User's stream listener.*/
	private final TwitterStreamer myStreamListener;
	/** Status that has been interacted with.*/
	private Status myStatus;
	/** Determine if the user has said yes.*/
	private boolean isYes;
	/** Determine if the user has picked a letter to play.*/
	private boolean hasPickedLetter;
	/** Determine if the user has already started playing.*/
	private boolean hasPlayed;
	/** TicTacToe game class.*/
	private TicTacToe myTicTacToe;
	/** List of statuses that have been tweeted.*/
	private List<Status> myPreviousStatusList;
	
	
	/**
	 * Constructor.
	 */
	public vechekaBot() {
		final TwitterStreamer ts = new TwitterStreamer();
		ts.addObserver(this);
		myStreamListener = ts;
		myPreviousStatusList = new ArrayList<>();
		
	}
	

	/** 
	 * Run the game on Twitter.
	 * @throws TwitterException 
	 */
    public void run() throws TwitterException {
    
    	Status s = twitter.updateStatus("Do you want to play a game of tic-tac-toe?");
    	myPreviousStatusList.add(s);
//    	List<Status> status = twitter.getHomeTimeline();
//    	for (Status st: status) {
//    		deleteStatus(st);
//    	}
    	myStreamListener.onStatus(s);
	
	}

	/**
	 * Delete a status from the main page.
	 * @param theStatus status to be deleted
	 * @throws TwitterException
	 */
	private void deleteStatus(final Status theStatus) throws TwitterException {
		twitter.destroyStatus(theStatus.getId());
	}
	
	
	/**
	 * Play and interact with the user according to their response via Twitter.
	 */
	private void interactWithUser(final Status theStatus) {
		final String[] response = theStatus.getText().split(" ");
		StatusUpdate s = null;
		final String replyTo = "Replying to @" + theStatus.getUser().getScreenName();
		if (contains(YES_LIST, response[1]) 
				&& !hasPickedLetter) {
			s = new StatusUpdate(replyTo + "\nDo you want to be \'X\' or \'O\'?"); 
			isYes = true;
			replyToTweet(s);
			
		} else if (response[1].equalsIgnoreCase("X")
				|| response[1].equalsIgnoreCase("O")
				&& isYes && !hasPickedLetter) {
			if (response[1].equalsIgnoreCase(myBotLetter)) {
				myBotLetter = "X";
				myUserLetter = "O";
			}
			myTicTacToe = new TicTacToe(myBotLetter, myUserLetter);
			hasPickedLetter = true;
			s = new StatusUpdate(replyTo + "\nYou're " + myUserLetter 
					+ "\nPick a space using the number\n" + myTicTacToe.toString());
			replyToTweet(s);
		} else if (contains(SPACE_NUMBER, response[1]) 
				&& hasPickedLetter) {
			// play the game
			hasPlayed = true;
			myTicTacToe.place(response[1]);  
			if (!myTicTacToe.getBotWin() 
					&& !myTicTacToe.getUserWin() ) {
				s = new StatusUpdate(replyTo + "\nYou're " + myUserLetter
						+ "\n" + myTicTacToe.toString()
						+ "\n" + myTicTacToe.showResult());
			} else if (myTicTacToe.getBotWin() 
					|| myTicTacToe.getUserWin()){
				s = new StatusUpdate(replyTo + "\n" + myTicTacToe.toString() 
					+ "\n" + myTicTacToe.showResult() +
						"\nDo you want to play again?");
				myTicTacToe.reset();
				reset();
			}	
			replyToTweet(s);	
		} else if (contains(NO_LIST, response[1])) {
			if (hasPlayed) {
				s = new StatusUpdate(replyTo + "\nThank you for playing, I'm going to take a nap now.");
				hasPlayed = false;
			} else s = new StatusUpdate(replyTo + "\nI guess I'll take a nap to hide my loneliness...");
			replyToTweet(s);
			myTicTacToe.resetScore();
			sleep();
		}
	}
	
	/**
	 * The bot goes to sleep for a certain amount of time.
	 */
	private void sleep() {
		try {
			Thread.sleep(30 * 100);
			reset();
			try {
				run();
			} catch (final TwitterException theE) {
				theE.printStackTrace();
			}
		} catch (final InterruptedException theE) {
			theE.printStackTrace();
		}
		
	}


	/**
	 * Reset everything back to when it first started.
	 * @throws TwitterException 
	 */
	private void reset() {
		isYes = false;
		hasPickedLetter = false;
		
//		for (final Status s: myPreviousStatusList) {
//			try {
//				deleteStatus(s);
//			} catch (final TwitterException theE) {
//				theE.printStackTrace();
//			}
//		}
		myPreviousStatusList.clear();
		
	}


	/**
	 * Check whether or not the word exist in the bot's dictionary.
	 * @param theList list of words in the bot's dictionary
	 * @param theWord word to be checked
	 * @return true if theWord exists in theList
	 */
	private boolean contains(final String[] theList, final String theWord) {
		boolean result = false;
		for (String word: theList) {
			if (word.equalsIgnoreCase(theWord)) {
				result = true;
				break;
			}
		}
		
		return result;
	}


	/**
	 * Reply to a response on a tweet.
	 * @param theStatus response to the user
	 */
	private void replyToTweet(final StatusUpdate theStatus) {
		try {
			final Status status = twitter.updateStatus(theStatus.inReplyToStatusId(myStatus.getId()));
			myPreviousStatusList.add(status);
			myStreamListener.onStatus(status);
		} catch (final TwitterException theE) {
			theE.printStackTrace();
		}
		
	}


	@Override
	public void update(final Observable theObeservable, final Object theData) {
		
		if (theData instanceof Status) {
			myStatus = (Status) theData;
			interactWithUser(myStatus);
		}
	}
	
	
	}


