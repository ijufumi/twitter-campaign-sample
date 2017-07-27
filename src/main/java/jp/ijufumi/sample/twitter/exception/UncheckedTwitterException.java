package jp.ijufumi.sample.twitter.exception;

import twitter4j.TwitterException;

public class UncheckedTwitterException extends RuntimeException {
    public UncheckedTwitterException(TwitterException e) {
        super(e);
    }

    public UncheckedTwitterException(String message, TwitterException e) {
        super(message, e);
    }
}
