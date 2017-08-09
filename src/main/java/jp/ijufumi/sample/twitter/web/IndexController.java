package jp.ijufumi.sample.twitter.web;

import jp.ijufumi.sample.twitter.exception.UncheckedTwitterException;
import jp.ijufumi.sample.twitter.service.CampaignService;
import jp.ijufumi.sample.twitter.service.TwitterService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

@Controller
@RequestMapping("/")
public class IndexController {

    final TwitterService twitterService;
    final CampaignService campaignService;
    final Logger logger;

    @Inject
    public IndexController(
            CampaignService campaignService,
            TwitterService twitterService,
            Logger logger) {
        this.campaignService = campaignService;
        this.twitterService = twitterService;
        this.logger = logger;
    }

    @RequestMapping
    public String index(Model model) {
        // Twitterのコネクションが取れない場合はSpring Social Twitterの機能を呼び出す
        if (!twitterService.isConnected()) {
            return "redirect:/connect/twitter";
        }

        String screenName = twitterService.getScreenName();
        boolean isFollowed = twitterService.checkFollowed(screenName);

        logger.info("followed by {} is {}.", screenName, isFollowed);

        model.addAttribute("follow", isFollowed);

        boolean isRetweeted = twitterService.checkRetweet(screenName);

        logger.info("retweeted by {} is {}.", screenName, isRetweeted);

        model.addAttribute("retweet", isRetweeted);
        return "index";
    }

    @RequestMapping("campaign/{campaignKey}")
    public String campaign(Model model, @PathVariable("campaignKey") String campaignKey) {


        return "";
    }

    @ExceptionHandler({UncheckedTwitterException.class, NullPointerException.class})
    public String exceptionHandle(RuntimeException e) {
        logger.error(e.getLocalizedMessage(), e);

        return "index";
    }
}
