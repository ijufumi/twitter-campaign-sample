package jp.ijufumi.sample.twitter.service;

import jp.ijufumi.sample.twitter.exception.UncheckedTwitterException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.social.TwitterProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import twitter4j.*;
import twitter4j.api.UsersResources;
import twitter4j.auth.AccessToken;
import twitter4j.conf.PropertyConfiguration;

import java.util.Objects;
import java.util.Properties;

/**
 * Twitterアクセス用のサービスクラス。
 */
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class TwitterService {

    /**
     * Twitter4J用のFactoryクラス
     */
    final TwitterFactory twitterFactory;
    /**
     * Spring Social Twitter用のコネクションリポジトリクラス
     */
    final ConnectionRepository connectionRepository;

    /**
     * Logger
     */
    final Logger logger;

    @Autowired
    public TwitterService(TwitterProperties twitterProperties,
                          Logger logger,
                          ConnectionRepository connectionRepository) {

        Properties properties = new Properties();
        properties.put("oauth.consumerKey", twitterProperties.getAppId());
        properties.put("oauth.consumerSecret", twitterProperties.getAppSecret());
        PropertyConfiguration configuration = new PropertyConfiguration(properties);

        logger.info("TwitterConfiguration: {}", ToStringBuilder.reflectionToString(configuration, ToStringStyle.JSON_STYLE));
        twitterFactory = new TwitterFactory(configuration);

        this.connectionRepository = connectionRepository;

        this.logger = logger;
    }

    /**
     * TwitterのIDを取得する
     *
     * @return
     */
    public long getTweetId() {
        try {
            return twitter().getId();
        }
        catch (TwitterException e) {
            throw new UncheckedTwitterException(e);
        }
    }

    /**
     * メールアドレスを取得する
     *
     * @return
     */
    public String getEmailAddress() {
        Twitter twitter = twitter();
        Objects.requireNonNull(twitter);
        try {
            return twitter().users().verifyCredentials().getEmail();
        }
        catch (TwitterException e) {
            throw new UncheckedTwitterException(e);
        }
    }

    /**
     * 指定したスクリーン名のユーザを、プロパティで設定したスクリーン名のユーザが
     * フォローしているかをチェックする。<br>
     * <br>
     * <b>今回の実装では未使用。</b>
     *
     * @param targetName フォローされているかをチェックするユーザ
     * @return 引数のユーザがフォローされていたらtrue、されていなかったらfalse
     */
    public boolean checkFollowing(String screenName, String targetName) {
        Twitter twitter = twitter();
        Objects.requireNonNull(twitter);

        try {
            return twitter.showFriendship(screenName, targetName).isSourceFollowingTarget();
        }
        catch (TwitterException e) {
            throw new UncheckedTwitterException(e);
        }
    }

    /**
     * 指定したスクリーン名のユーザが、プロパティで設定したスクリーン名のユーザに
     * フォローしているかをチェックする。<br>
     *
     * @param targetName フォローしているかをチェックするユーザ
     * @return 引数のユーザがフォローしていたらtrue、していなかったらfalse
     */
    public boolean checkFollowed(String screenName, String targetName) {
        Twitter twitter = twitter();
        Objects.requireNonNull(twitter);

        try {
            return twitter.showFriendship(screenName, targetName).isSourceFollowedByTarget();
        }
        catch (TwitterException e) {
            throw new UncheckedTwitterException(e);
        }
    }

    /**
     * 指定したスクリーン名のユーザが、プロパティで設定したツイートを
     * リツイートしているかチェックする<br>
     * ただし、タイムラインの最新の1000件しかチェックしていないので、
     * リツイートしてかなり経っているものはうまく判断されない可能性がある。
     *
     * @param targetName リツイートしているかチェックするユーザ
     * @return 引数のユーザがリツイートしていたらtrue、していなかったらfalse
     */
    public boolean checkRetweet(long tweetId, String targetName) {
        Twitter twitter = twitter();
        Objects.requireNonNull(twitter);

        try {
            long pageNation = 0;
            for (int i = 0; i < 5; i++) {
                Paging paging = (pageNation > 1) ? new Paging(pageNation) : new Paging();
                ResponseList<Status> responseList = twitter.timelines().getUserTimeline(targetName, paging);
                if (CollectionUtils.isEmpty(responseList)) {
                    break;
                }

                boolean flag = responseList.stream().anyMatch(x -> x.isRetweet() && x.getRetweetedStatus().getId() == tweetId);

                if (flag) {
                    return true;
                }
                pageNation = responseList.get(responseList.size() - 1).getId();
            }

            return false;
        }
        catch (TwitterException e) {
            throw new UncheckedTwitterException(e);
        }
    }

    /**
     * Twitterへの接続が存在するかチェックする
     *
     * @return
     */
    public boolean isConnected() {
        return twitter() != null;
    }

    /**
     * スクリーン名を取得する
     *
     * @return 今アクセスしているユーザのスクリーン名
     */
    public String getScreenName() {
        Twitter twitter = twitter();
        Objects.requireNonNull(twitter);

        try {
            return twitter.getScreenName();
        }
        catch (TwitterException e) {
            throw new UncheckedTwitterException(e);
        }
    }

    /**
     * キャンペーン画面に表示するための埋め込み用HTMLを取得する
     *
     * @return
     */
    public String getEmbedHTML(String screenName, long statusId) {
        Twitter twitter = twitter();
        Objects.requireNonNull(twitter);

        try {
            OEmbedRequest request = new OEmbedRequest(statusId, String.format("https://twitter.com/%s/status/%d", screenName, statusId));
            request.setHideThread(true);
            request.setMaxWidth(1000);
            request.setHideMedia(false);
            logger.debug("[EmbedHTML] request:{}", request);
            OEmbed embed = twitter.tweets().getOEmbed(request);
            return embed.getHtml();
        }
        catch (TwitterException e) {
            throw new UncheckedTwitterException(e);
        }
    }

    /**
     * Twitter API呼び出し用にTwitter4JのTwitterインスタンスを生成する
     *
     * @return Twitterインスタンス
     */
    private Twitter twitter() {
        // アクセストークンを取得するために、Twitterのコネクション情報を取得する
        Connection<org.springframework.social.twitter.api.Twitter> twitterConnection = connectionRepository.findPrimaryConnection(org.springframework.social.twitter.api.Twitter.class);

        if (twitterConnection == null) {
            return null;
        }

        ConnectionData connectionData = twitterConnection.createData();

        logger.info("ConnectionData: {}", ToStringBuilder.reflectionToString(connectionData, ToStringStyle.JSON_STYLE));

        AccessToken token = new AccessToken(connectionData.getAccessToken(), connectionData.getSecret());
        return twitterFactory.getInstance(token);
    }
}
