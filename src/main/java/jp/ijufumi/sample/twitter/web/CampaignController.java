package jp.ijufumi.sample.twitter.web;

import jp.ijufumi.sample.twitter.domain.entity.TCampaign;
import jp.ijufumi.sample.twitter.service.CampaignService;
import jp.ijufumi.sample.twitter.service.TwitterService;
import jp.ijufumi.sample.twitter.web.common.ControllerBase;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/campaign")
public class CampaignController extends ControllerBase {

    public CampaignController(
            TwitterService twitterService,
            CampaignService campaignService,
            Logger logger
    ){
        super(twitterService, campaignService, logger);
    }

    /**
     * キャンペーン詳細画面表示
     *
     * @param model
     * @param campaignKey
     * @return
     */
    @RequestMapping("{campaignKey}")
    public String index(Model model, @PathVariable("campaignKey") String campaignKey) {
        Optional<TCampaign> campaignOpt = campaignService.getCampaign(campaignKey);
        if (!campaignOpt.isPresent()) {
            return "error";
        }

        TCampaign campaign = campaignOpt.get();
        model.addAttribute("campaign", campaign);
        model.addAttribute("embedHtml", twitterService.getEmbedHTML(campaign.getScreenName(), campaign.getStatusId()));
        return "campaign";
    }

    @RequestMapping("{campaignKey}/form")
    public String form(Model model,
                       @PathVariable("campaignKey") String campaignKey
    ) {
        Optional<TCampaign> campaignOpt = campaignService.getCampaign(campaignKey);
        if (!campaignOpt.isPresent()) {
            return "error";
        }

                
        return "campaign_form";
    }
}
