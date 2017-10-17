package jp.ijufumi.sample.twitter.util;

import jp.ijufumi.sample.twitter.domain.entity.TCampaign;
import jp.ijufumi.sample.twitter.domain.value.PrizeStatusObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class CampaignUtil {
    /**
     * 抽選を行う。
     * 今までのトータルカウントと当選数にそれぞれ1を足したもので当選率を計算し、
     * 設定されている当選率より少なければ当選とし、多ければ落選にする。
     *
     * @param winningRate 当選率
     * @param winningCount 当選数
     * @param totalCount トータル数
     * @return 抽選結果
     */
    public static PrizeStatusObject drawLots(int winningRate, int winningCount, int totalCount) {
        BigDecimal calculated = BigDecimal.valueOf(winningCount + 1)
                .divide(BigDecimal.valueOf(totalCount + 1), 2, RoundingMode.UP)
                .multiply(BigDecimal.valueOf(100));

        if (calculated.intValue() <= winningRate) {
            return PrizeStatusObject.WIN;
        }

        return PrizeStatusObject.LOSE;
    }

    public static boolean validCampaign(TCampaign campaign) {
        return validCampaign(campaign, LocalDateTime.now());
    }

    public static boolean validCampaign(TCampaign campaign, LocalDateTime now) {
        if (campaign == null) {
            return false;
        }

        if (campaign.getValidStartDate().isAfter(now) ||
                campaign.getValidEndDate().isBefore(now)) {
            return false;
        }

        return true;
    }
}
