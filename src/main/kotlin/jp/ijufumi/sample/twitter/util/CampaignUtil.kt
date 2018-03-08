package jp.ijufumi.sample.twitter.util

import jp.ijufumi.sample.twitter.domain.entity.TCampaign
import jp.ijufumi.sample.twitter.domain.value.PrizeStatusObject

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

object CampaignUtil {
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
    @JvmStatic
    fun drawLots(winningRate: Int, winningCount: Int, totalCount: Int): PrizeStatusObject {
        val calculated = BigDecimal.valueOf((winningCount + 1).toLong())
                .divide(BigDecimal.valueOf((totalCount + 1).toLong()), 2, RoundingMode.UP)
                .multiply(BigDecimal.valueOf(100))

        return if (calculated.toInt() <= winningRate) {
            PrizeStatusObject.WIN
        } else PrizeStatusObject.LOSE

    }

    @JvmOverloads
    @JvmStatic
    fun validCampaign(campaign: TCampaign?, now: LocalDateTime = LocalDateTime.now()): Boolean {
        if (campaign == null) {
            return false
        }

        return !(campaign.validStartDate.isAfter(now) || campaign.validEndDate.isBefore(now))
    }
}
