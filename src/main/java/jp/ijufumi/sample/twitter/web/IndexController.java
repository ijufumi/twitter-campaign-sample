package jp.ijufumi.sample.twitter.web;

import jp.ijufumi.sample.twitter.service.TwitterService;
import org.slf4j.Logger;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

@Controller
@RequestMapping("/")
public class IndexController {

    private final Twitter twitter;
    private final TwitterService twitterService;
    private final Logger logger;

    @Inject
    public IndexController(Twitter twitter,
                           TwitterService twitterService,
                           Logger logger) {
        this.twitter = twitter;
        this.twitterService = twitterService;
        this.logger = logger;
    }

    @RequestMapping
    public String index(Model model) {
        if (twitterService.twitter() == null) {
            return "redirect:/connect/twitter";
        }

        String screenName = twitter.userOperations().getScreenName();
        boolean isFollowed = twitterService.checkFollowed(screenName);

        logger.info("followed by {} is {}.", screenName, isFollowed);

        model.addAttribute("follow", isFollowed);

        boolean isRetweeted = twitterService.checkRetweet(screenName);

        logger.info("retweeted by {} is {}.", screenName, isRetweeted);

        model.addAttribute("retweet", isRetweeted);
        return "index";
    }
}
