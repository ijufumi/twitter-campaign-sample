package jp.ijufumi.sample.twitter.service;

import jp.ijufumi.sample.twitter.domain.dao.CampaignResultDao;
import jp.ijufumi.sample.twitter.domain.entity.CampaignResult;
import jp.ijufumi.sample.twitter.domain.value.PrizeStatusObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CampaignService {
    final CampaignResultDao campaignResultDao;
    final Logger logger;

    @Autowired
    public CampaignService(CampaignResultDao campaignResultDao,
                           Logger logger) {
        this.campaignResultDao = campaignResultDao;
        this.logger = logger;
    }

    public String register(long twitterId, PrizeStatusObject prizeStatus) {
        String accessKey = "";

        if (PrizeStatusObject.WIN.equals(prizeStatus)) {
            accessKey = RandomStringUtils.random(20, "ABCEDFGHIJKLMNOPQRSTUVWXYZ0123456789abcedfghijklmnopqrstuvwxyz");
        }

        CampaignResult campaignResult = new CampaignResult(twitterId, prizeStatus, "", "", LocalDateTime.now());

        campaignResultDao.insert(campaignResult);

        return accessKey;
    }

    public Optional<CampaignResult> getResult(long twitterId, String accessKey) {
        CampaignResult campaignResult = campaignResultDao.selectById(twitterId);
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

    public void updateEmailAddress(long twitterId, String emailAddress) {
        CampaignResult campaignResult = campaignResultDao.selectById(twitterId);
        if (campaignResult == null || campaignResult.getPrizeStatus().equals(PrizeStatusObject.LOSE)) {
            logger.info("CampaignResult is invalid.[{}]", twitterId);
            return;
        }

        CampaignResult newCampaignResult = new CampaignResult(
                campaignResult.getTwitterId(),
                campaignResult.getPrizeStatus(),
                emailAddress,
                campaignResult.getAccessKey(),
                campaignResult.getCreatedAt()
        );

        campaignResultDao.update(newCampaignResult);
    }
}
