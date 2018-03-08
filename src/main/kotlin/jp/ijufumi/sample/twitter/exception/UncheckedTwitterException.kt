package jp.ijufumi.sample.twitter.exception

import twitter4j.TwitterException

class UncheckedTwitterException (message: String, e: TwitterException) : RuntimeException(message, e) {
    constructor(e: TwitterException) : this("", e)
}
