package jp.ijufumi.sample.twitter.service;

import jp.ijufumi.sample.twitter.domain.dao.CampaignDao;
import jp.ijufumi.sample.twitter.domain.dao.CampaignResultDao;
import jp.ijufumi.sample.twitter.domain.entity.Campaign;
import jp.ijufumi.sample.twitter.domain.entity.CampaignResult;
import jp.ijufumi.sample.twitter.domain.value.PrizeStatusObject;
import org.apache.commons.lang3.RandomStringUtils;
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
        Campaign campaign = campaignDao.selectByCampaignKey(campaignKey, LocalDateTime.now());

        return Optional.ofNullable(campaign);
    }

    // 抽選結果を登録する
    public String register(int campaignId, long twitterId, PrizeStatusObject prizeStatus) {
        String accessKey = "";

        if (PrizeStatusObject.WIN.equals(prizeStatus)) {
            accessKey = RandomStringUtils.random(20, "ABCEDFGHIJKLMNOPQRSTUVWXYZ0123456789abcedfghijklmnopqrstuvwxyz");
        }

        CampaignResult campaignResult = new CampaignResult(-1L, campaignId, twitterId, prizeStatus, "", "", LocalDateTime.now());

        campaignResultDao.insert(campaignResult);

        return accessKey;
    }

    // 結果を取得する
    public Optional<CampaignResult> getResult(long campaignId, long twitterId, String accessKey) {
        CampaignResult campaignResult = campaignResultDao.selectById(campaignId, twitterId);
        Optional<CampaignResult> result = Optional.empty();
        if (campaignResult == null) {
            logger.info("CampaignResult is not exists.[{}]", twitterId);
            return result;
        }

        if (campaignResult.getPrizeStatus().equals(PrizeStatusObject.LOSE)) {
            logger.info("CampaignResult is not win.[{}]", twitterId);
            return result;
        }

        if (!campaignResult.getAccessKey().equals(accessKey)) {
            logger.info("CampaignResult is not equals accessKey. required[{}] actual[{}]", twitterId, accessKey);
            return result;
        }

        return Optional.of(campaignResult);
    }

    // メールアドレスを入力する
    public void updateEmailAddress(long campaignId, long twitterId, String emailAddress) {
        CampaignResult campaignResult = campaignResultDao.selectById(campaignId, twitterId);
        if (campaignResult == null || campaignResult.getPrizeStatus().equals(PrizeStatusObject.LOSE)) {
            logger.info("CampaignResult is invalid.[{}]", twitterId);
            return;
        }

        CampaignResult newCampaignResult = new CampaignResult(
                campaignResult.getResultId(),
                campaignResult.getCampaignId(),
                campaignResult.getTwitterId(),
                campaignResult.getPrizeStatus(),
                emailAddress,
                campaignResult.getAccessKey(),
                campaignResult.getCreatedAt()
        );

        campaignResultDao.update(newCampaignResult);
    }
}
