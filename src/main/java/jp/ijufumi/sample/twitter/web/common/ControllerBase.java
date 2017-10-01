package jp.ijufumi.sample.twitter.web.common;


import jp.ijufumi.sample.twitter.exception.UncheckedTwitterException;
import jp.ijufumi.sample.twitter.service.CampaignService;
import jp.ijufumi.sample.twitter.service.TwitterService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ControllerBase {
    protected TwitterService twitterService;
    protected CampaignService campaignService;
    protected Logger logger;

    protected ControllerBase(
            TwitterService twitterService,
            CampaignService campaignService,
            Logger logger
    ){
        this.twitterService = twitterService;
        this.campaignService = campaignService;
        this.logger = logger;
    }

    @ExceptionHandler({UncheckedTwitterException.class, NullPointerException.class})
    public String exceptionHandle(RuntimeException e) {
        logger.error(e.getLocalizedMessage(), e);

        return "index";
    }
}
