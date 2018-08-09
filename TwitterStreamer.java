import java.util.Observable;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import twitter4j.conf.ConfigurationBuilder;
/**
 * A class that implements twitter stream class to stream live a Twitter's account.
 * @author Vecheka Chhourn
 *
 */
public class TwitterStreamer extends Observable implements UserStreamListener {


	
	/**
	 * Constructor that initialize stream listener.
	 */
	public TwitterStreamer() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("vxcbzB8akWf5vJdCknHEGP6lV")
		.setOAuthConsumerSecret("JXploEIvuCaNi2QBN0NICaHZNWFVUJ6V8CFrOAbZHyJ87hWj73")
		.setOAuthAccessToken("841544113841364992-7sNHTpzigzAxenvCbSeEoL2ura313zj")
		.setOAuthAccessTokenSecret("jKwOOWV6OnQGrJMwwYAaswij0HpKMtbODuk6gued3jrqa");
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream.addListener(this);
		twitterStream.user();
	}
	
	
	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatus(final Status theStatus) {
		
		setChanged();
		notifyObservers(theStatus);
//		final String[] response = theStatus.getText().split(" ");
//		if (response[1].equalsIgnoreCase("yes")) {
//			try {
//				myTwitter.updateStatus(new StatusUpdate("Replying to @" + theStatus.getUser().getScreenName()
//						+ "\nDo you want to be \'X\' or \'O\'?").inReplyToStatusId(theStatus.getId()));
//			} catch (TwitterException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		System.out.println(theStatus.getUser().getScreenName());
		
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onException(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBlock(User arg0, User arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeletionNotice(long arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDirectMessage(DirectMessage arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFavorite(User arg0, User arg1, Status arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFavoritedRetweet(User arg0, User arg1, Status arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFollow(User arg0, User arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFriendList(long[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onQuotedTweet(User arg0, User arg1, Status arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRetweetedRetweet(User arg0, User arg1, Status arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnblock(User arg0, User arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnfavorite(User arg0, User arg1, Status arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnfollow(User arg0, User arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserDeletion(long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListCreation(User arg0, UserList arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListDeletion(User arg0, UserList arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListMemberAddition(User arg0, User arg1, UserList arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListMemberDeletion(User arg0, User arg1, UserList arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListSubscription(User arg0, User arg1, UserList arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListUnsubscription(User arg0, User arg1, UserList arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListUpdate(User arg0, UserList arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserProfileUpdate(User arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserSuspension(long arg0) {
		// TODO Auto-generated method stub
		
	}

}
