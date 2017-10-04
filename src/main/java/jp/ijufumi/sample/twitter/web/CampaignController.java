package jp.ijufumi.sample.twitter.web;

import jp.ijufumi.sample.twitter.domain.entity.TCampaign;
import jp.ijufumi.sample.twitter.domain.entity.TCampaignResult;
import jp.ijufumi.sample.twitter.domain.value.PrizeStatusObject;
import jp.ijufumi.sample.twitter.form.LotsResult;
import jp.ijufumi.sample.twitter.form.RegisterInfo;
import jp.ijufumi.sample.twitter.service.CampaignService;
import jp.ijufumi.sample.twitter.service.TwitterService;
import jp.ijufumi.sample.twitter.web.common.ControllerBase;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    /**
     * 抽選を実施する
     *
     * @param campaignKey
     * @return
     */
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

    /**
     * 個人情報（メールアドレス）を入力する画面を表示する
     *
     * @param model
     * @param campaignKey
     * @return
     */
    @GetMapping("{campaignKey}/form")
    public String form(Model model,
                       @PathVariable("campaignKey") String campaignKey
    ) {
        Optional<TCampaign> campaignOpt = campaignService.getCampaign(campaignKey);
        if (!campaignOpt.isPresent()) {
            return "error";
        }

        model.addAttribute("emailAddress", twitterService.getEmailAddress());

        return "campaign_form";
    }

    /**
     * 個人情報（メールアドレス）を登録する
     *
     * @param model
     * @param campaignKey
     * @param registerInfo
     * @param result
     * @return
     */
    @PostMapping("{campaignKey}/register")
    public String register(Model model,
                           @PathVariable("campaignKey") String campaignKey,
                           @RequestBody RegisterInfo registerInfo,
                           BindingResult result) {

        Optional<TCampaign> campaignOpt = campaignService.getCampaign(campaignKey);
        if (!campaignOpt.isPresent()) {
            return "error";
        }
        // バリデーションエラーがある場合
        if (result.hasErrors()) {
            model.addAttribute("emailAddress", registerInfo.getEmailAddress());
            return "campaign_form";
        }

        long twitterId = twitterService.getTweetId();
        campaignService.updateEmailAddress(campaignOpt.get().getCampaignId(), twitterId, registerInfo.getEmailAddress());

        return "redirect:/campaign/" + campaignKey + "/result";
    }

    /**
     * 結果画面を表示する
     *
     * @param model
     * @param campaignKey
     * @return
     */
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
