package jp.ijufumi.sample.twitter.web;

import jp.ijufumi.sample.twitter.domain.entity.TCampaign;
import jp.ijufumi.sample.twitter.domain.entity.TCampaignResult;
import jp.ijufumi.sample.twitter.domain.value.PrizeStatusObject;
import jp.ijufumi.sample.twitter.form.LotsResult;
import jp.ijufumi.sample.twitter.service.CampaignService;
import jp.ijufumi.sample.twitter.service.TwitterService;
import jp.ijufumi.sample.twitter.web.common.ControllerBase;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("{campaignKey}")
    public String index(Model model, @PathVariable("campaignKey") String campaignKey) {
        Optional<TCampaign> campaignOpt = campaignService.getCampaign(campaignKey);
        if (!campaignOpt.isPresent()) {
            return "error";
        }

        TCampaign campaign = campaignOpt.get();

        long twitterId = twitterService.getTweetId();
        Optional<TCampaignResult> campaignResultOpt = campaignService.getResult(campaign.getCampaignId(), twitterId);

        // 抽選済みの場合は、結果ページに遷移する
        if (campaignResultOpt.isPresent()) {

            TCampaignResult campaignResult = campaignResultOpt.get();
            PrizeStatusObject prizeStatus = campaignResult.getPrizeStatus();

            if (PrizeStatusObject.WIN.equals(prizeStatus)) {
                // 当選かつメールアドレス未入力の場合は入力画面へ
                if (StringUtils.isEmpty(campaignResult.getEmailAddress())) {
                    return "redirect:/campaign/" + campaignKey + "/form";
                }
            }

            return "redirect:/campaign/" + campaignKey + "/result";
        }

        model.addAttribute("campaign", campaign);
        model.addAttribute("embedHtml", twitterService.getEmbedHTML(campaign.getScreenName(), campaign.getStatusId()));
        return "campaign";
    }

    @PostMapping("{campaignKey}/lots")
    @ResponseBody
    public LotsResult lots(@RequestParam("campaignKey") String campaignKey) {
        Optional<TCampaign> campaignOpt = campaignService.getCampaign(campaignKey);
        LotsResult lotsResult = new LotsResult();
        if (!campaignOpt.isPresent()) {
            lotsResult.setError(true);
            return lotsResult;
        }

        long twitterId = twitterService.getTweetId();
        PrizeStatusObject prizeStatusObject = campaignService.drawLots(campaignOpt.get().getCampaignId(), twitterId);

        if (PrizeStatusObject.WIN.equals(prizeStatusObject)) {
            lotsResult.setResult(true);
        } else {
            lotsResult.setResult(false);
        }

        return lotsResult;
    }

    @GetMapping("{campaignKey}/form")
    public String form(Model model,
                       @PathVariable("campaignKey") String campaignKey
    ) {
        Optional<TCampaign> campaignOpt = campaignService.getCampaign(campaignKey);
        if (!campaignOpt.isPresent()) {
            return "error";
        }

        return "campaign_form";
    }

    @GetMapping("{campaignKey}/result")
    public String result(Model model, @PathVariable("campaignKey") String campaignKey) {
        Optional<TCampaign> campaignOpt = campaignService.getCampaign(campaignKey);
        if (!campaignOpt.isPresent()) {
            return "error";
        }

        TCampaign campaign = campaignOpt.get();

        long twitterId = twitterService.getTweetId();
        Optional<TCampaignResult> campaignResultOpt = campaignService.getResult(campaign.getCampaignId(), twitterId);

        // 結果がない人はキャンペーントップへ遷移
        if (!campaignResultOpt.isPresent()) {
            return "redirect:/campaign/" + campaignKey;
        }

        model.addAttribute("prizeStatus", campaignResultOpt.get().getPrizeStatus().getValue());

        return "rampaign_result";
    }
}
