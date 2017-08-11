package jp.ijufumi.sample.twitter.service;

import jp.ijufumi.sample.twitter.domain.dao.CampaignDao;
import jp.ijufumi.sample.twitter.domain.dao.CampaignResultDao;
import jp.ijufumi.sample.twitter.domain.entity.Campaign;
import jp.ijufumi.sample.twitter.domain.entity.CampaignResult;
import jp.ijufumi.sample.twitter.domain.value.PrizeStatusObject;
import jp.ijufumi.sample.twitter.util.CampaignUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {
    final CampaignDao campaignDao;
    final CampaignResultDao campaignResultDao;
    final Logger logger;

    @Autowired
    public CampaignService(
            CampaignDao campaignDao,
            CampaignResultDao campaignResultDao,
            Logger logger) {
        this.campaignDao = campaignDao;
        this.campaignResultDao = campaignResultDao;
        this.logger = logger;
    }

    /**
     * キャンペーンのリストを取得する
     * @return
     */
    public List<Campaign> getCampaignList() {
        return campaignDao.selectValidList(LocalDateTime.now());
    }

    // キャンペーンを取得する
    public Optional<Campaign> getCampaign(String campaignKey) {
        return campaignDao.selectByCampaignKey(campaignKey, LocalDateTime.now());
    }

    public PrizeStatusObject drawLots(int campaignId, long twitterId) {
        Optional<CampaignResult> campaignResultOpt = campaignResultDao.selectById(campaignId, twitterId);

        if (campaignResultOpt.isPresent()) {
            return campaignResultOpt.get().getPrizeStatus();
        }

        Optional<Campaign> campaignOpt = campaignDao.selectByIdWithLock(campaignId);

        Campaign campaign = campaignOpt.get();
        PrizeStatusObject prizeStatus = CampaignUtil.drawLots(campaign.getWinningRate(), campaign.getWinningCount(), campaign.getTotalCount());

        int winningCount = campaign.getWinningCount();
        String accessKey = "";
        if (PrizeStatusObject.WIN.equals(prizeStatus)) {
            winningCount++;
            accessKey = CampaignUtil.generateAccessKey();
        }

        Campaign newCampaign = campaign.copyOf(winningCount, campaign.getTotalCount() + 1, LocalDateTime.now());
        campaignDao.update(newCampaign);

        CampaignResult campaignResult = new CampaignResult(
                -1L,
                campaignId,
                twitterId,
                prizeStatus,
                "",
                "",
                LocalDateTime.now(),
                LocalDateTime.now());
        campaignResultDao.insert(campaignResult);

        return prizeStatus;
    }

    // 結果を取得する
    public Optional<CampaignResult> getResult(int campaignId, long twitterId) {
        Optional<CampaignResult> campaignResultOpt = campaignResultDao.selectById(campaignId, twitterId);
        if (!campaignResultOpt.isPresent()) {
            logger.info("CampaignResult is not exists.[{}]", twitterId);
            return campaignResultOpt;
        }

        CampaignResult campaignResult = campaignResultOpt.get();

        if (campaignResult.getPrizeStatus().equals(PrizeStatusObject.LOSE)) {
            logger.info("CampaignResult is not win.[{}]", twitterId);
            return campaignResultOpt;
        }

        return Optional.of(campaignResult);
    }

    // メールアドレスを入力する
    public void updateEmailAddress(int campaignId, long twitterId, String emailAddress) {
        Optional<CampaignResult> campaignResultOpt = campaignResultDao.selectById(campaignId, twitterId);

        if (!campaignResultOpt.isPresent()) {
            logger.info("CampaignResult is invalid.[{}]", twitterId);
            return;
        }
        CampaignResult campaignResult = campaignResultOpt.get();
        if (campaignResult.getPrizeStatus().equals(PrizeStatusObject.LOSE)) {
            logger.info("CampaignResult is invalid.[{}]", twitterId);
            return;
        }

        CampaignResult newCampaignResult = campaignResult.copyOf(emailAddress, LocalDateTime.now());
        campaignResultDao.update(newCampaignResult);
    }


}
