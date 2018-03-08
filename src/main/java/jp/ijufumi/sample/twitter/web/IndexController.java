package jp.ijufumi.sample.twitter.web;

import jp.ijufumi.sample.twitter.domain.entity.TCampaign;
import jp.ijufumi.sample.twitter.service.CampaignService;
import jp.ijufumi.sample.twitter.service.TwitterService;
import jp.ijufumi.sample.twitter.web.common.ControllerBase;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController extends ControllerBase {

    public IndexController(
            TwitterService twitterService,
            CampaignService campaignService,
            Logger logger
    ){
        super(twitterService, campaignService, logger);
    }

    /**
     * トップ画面表示
     *
     * @param model
     * @return
     */
    @GetMapping
    public String index(Model model) {
        List<TCampaign> campaignList = campaignService.getCampaignList();
        model.addAttribute("campaignList", campaignList);

        logger.debug("campaignList:{}", campaignList);
        return "index";
    }
}
