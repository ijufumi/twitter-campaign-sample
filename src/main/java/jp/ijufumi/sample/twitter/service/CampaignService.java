package jp.ijufumi.sample.twitter.service;

import jp.ijufumi.sample.twitter.domain.dao.CampaignDao;
import jp.ijufumi.sample.twitter.domain.dao.CampaignResultDao;
import jp.ijufumi.sample.twitter.domain.entity.TCampaign;
import jp.ijufumi.sample.twitter.domain.entity.TCampaignResult;
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
     *
     * @return
     */
    public List<TCampaign> getCampaignList() {
        return campaignDao.selectValidList(LocalDateTime.now());
    }

    // キャンペーンを取得する
    public Optional<TCampaign> getCampaign(String campaignKey) {
        return campaignDao.selectByCampaignKey(campaignKey, LocalDateTime.now());
    }

    public PrizeStatusObject drawLots(int campaignId, long twitterId) {
        Optional<TCampaignResult> campaignResultOpt = campaignResultDao.selectById(campaignId, twitterId);

        if (campaignResultOpt.isPresent()) {
            return campaignResultOpt.get().getPrizeStatus();
        }

        Optional<TCampaign> campaignOpt = campaignDao.selectByIdWithLock(campaignId);

        TCampaign campaign = campaignOpt.get();
        PrizeStatusObject prizeStatus = CampaignUtil.drawLots(campaign.getWinningRate(), campaign.getWinningCount(), campaign.getTotalCount());

        int winningCount = campaign.getWinningCount();
        if (PrizeStatusObject.WIN.equals(prizeStatus)) {
            winningCount++;
        }

        TCampaign newCampaign = campaign.copyOf(winningCount, campaign.getTotalCount() + 1, LocalDateTime.now());
        campaignDao.update(newCampaign);

        TCampaignResult campaignResult = new TCampaignResult(
                -1,
                campaignId,
                twitterId,
                prizeStatus,
                "",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        campaignResultDao.insert(campaignResult);

        return prizeStatus;
    }

    // 結果を取得する
    public Optional<TCampaignResult> getResult(int campaignId, long twitterId) {
        Optional<TCampaignResult> campaignResultOpt = campaignResultDao.selectById(campaignId, twitterId);
        if (!campaignResultOpt.isPresent()) {
            logger.info("TCampaignResult is not exists.[{}]", twitterId);
            return campaignResultOpt;
        }

        TCampaignResult campaignResult = campaignResultOpt.get();

        if (campaignResult.getPrizeStatus().equals(PrizeStatusObject.LOSE)) {
            logger.info("TCampaignResult is not win.[{}]", twitterId);
            return campaignResultOpt;
        }

        return Optional.of(campaignResult);
    }

    // メールアドレスを入力する
    public void updateEmailAddress(int campaignId, long twitterId, String emailAddress) {
        Optional<TCampaignResult> campaignResultOpt = campaignResultDao.selectById(campaignId, twitterId);

        if (!campaignResultOpt.isPresent()) {
            logger.info("TCampaignResult is invalid.[{}]", twitterId);
            return;
        }
        TCampaignResult campaignResult = campaignResultOpt.get();
        if (campaignResult.getPrizeStatus().equals(PrizeStatusObject.LOSE)) {
            logger.info("TCampaignResult is invalid.[{}]", twitterId);
            return;
        }

        TCampaignResult newCampaignResult = campaignResult.copyOf(emailAddress, LocalDateTime.now());
        campaignResultDao.update(newCampaignResult);
    }


}
