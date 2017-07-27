package jp.ijufumi.sample.twitter.service;

import jp.ijufumi.sample.twitter.exception.UncheckedTwitterException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.PropertyConfiguration;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Properties;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class TwitterService {

    final TwitterFactory twitterFactory;
    final ConnectionRepository connectionRepository;

    final String screenName;
    final long tweetId;

    final Logger logger;
    @Autowired
    public TwitterService(@Value("${spring.social.twitter.appId}") String appId,
                          @Value("${spring.social.twitter.appSecret}") String secret,
                          @Value("${twitter.screenName}") String screenName,
                          @Value("${twitter.tweetId}") long tweetId,
                          Logger logger,
                          ConnectionRepository connectionRepository) {

        this.screenName = screenName;
        this.tweetId = tweetId;

        Properties properties = new Properties();
        properties.put("oauth.consumerKey", appId);
        properties.put("oauth.consumerSecret", secret);

        PropertyConfiguration configuration = new PropertyConfiguration(properties);

        logger.info("TwitterConfiguration: {}", ToStringBuilder.reflectionToString(configuration, ToStringStyle.JSON_STYLE));
        twitterFactory = new TwitterFactory(configuration);
        this.connectionRepository = connectionRepository;
        this.logger = logger;
    }

    public boolean checkFollowing(String targetName) {
        Twitter twitter = twitter();
        Objects.requireNonNull(twitter);

        try {
            return twitter.showFriendship(screenName, targetName).isSourceFollowingTarget();
        } catch (TwitterException e) {
            throw new UncheckedTwitterException(e);
        }
    }

    public boolean checkFollowed(String targetName) {
        Twitter twitter = twitter();
        Objects.requireNonNull(twitter);

        try {
            return twitter.showFriendship(screenName, targetName).isSourceFollowedByTarget();
        } catch (TwitterException e) {
            throw new UncheckedTwitterException(e);
        }
    }

    public boolean checkRetweet(String targetName) {
        Twitter twitter = twitter();
        Objects.requireNonNull(twitter);

        try {
            long pageNation = 0;
            for (int i=0;i < 5;i++) {
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

            return  false;
        } catch (TwitterException e) {
            throw new UncheckedTwitterException(e);
        }
    }

    public Twitter twitter() {
        Connection<org.springframework.social.twitter.api.Twitter> twitterConnection = connectionRepository.findPrimaryConnection(org.springframework.social.twitter.api.Twitter.class);

        if (twitterConnection == null) {
            return null;
        }

        ConnectionData connectionData = twitterConnection.createData();

        logger.info("ConnectionData: {}", ToStringBuilder.reflectionToString(connectionData, ToStringStyle.JSON_STYLE));

        AccessToken token = new AccessToken(connectionData.getAccessToken(), connectionData.getSecret());
        return twitterFactory.getInstance(token);
    }

    @PostConstruct
    public void postConstruct() {
        logger.info("postConstruct() called. {}");
    }
}
