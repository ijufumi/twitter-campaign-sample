package jp.ijufumi.sample.twitter.domain.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;


import java.time.LocalDateTime;

@Entity(immutable = true)
public class TCampaign extends EntityCommon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int campaignId;

    private final String campaignKey;
    private final String campaignName;
    private final String bannerImagePath;
    private final String screenName;
    private final long statusId;
    private final LocalDateTime validStartDate;
    private final LocalDateTime validEndDate;
    private final LocalDateTime displayStartDate;
    private final LocalDateTime displayEndDate;
    private final int winningRate;
    private final int winningCount;
    private final int totalCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TCampaign(int campaignId, String campaignKey, String campaignName, String bannerImagePath, String screenName, long statusId, LocalDateTime validStartDate, LocalDateTime validEndDate, LocalDateTime displayStartDate, LocalDateTime displayEndDate, int winningRate, int winningCount, int totalCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.campaignId = campaignId;
        this.campaignKey = campaignKey;
        this.campaignName = campaignName;
        this.bannerImagePath = bannerImagePath;
        this.screenName = screenName;
        this.statusId = statusId;
        this.validStartDate = validStartDate;
        this.validEndDate = validEndDate;
        this.displayStartDate = displayStartDate;
        this.displayEndDate = displayEndDate;
        this.winningRate = winningRate;
        this.winningCount = winningCount;
        this.totalCount = totalCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public String getCampaignKey() {
        return campaignKey;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getBannerImagePath() {
        return bannerImagePath;
    }

    public String getScreenName() {
        return screenName;
    }

    public long getStatusId() {
        return statusId;
    }

    public LocalDateTime getValidStartDate() {
        return validStartDate;
    }

    public LocalDateTime getValidEndDate() {
        return validEndDate;
    }

    public LocalDateTime getDisplayStartDate() {
        return displayStartDate;
    }

    public LocalDateTime getDisplayEndDate() {
        return displayEndDate;
    }

    public int getWinningRate() {
        return winningRate;
    }

    public int getWinningCount() {
        return winningCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public TCampaign copyOf(int winningCount, int totalCount, LocalDateTime updatedAt) {
        return new TCampaign(
              campaignId,
              campaignKey,
              campaignName,
              bannerImagePath,
              screenName,
              statusId,
              validStartDate,
              validEndDate,
              displayStartDate,
              displayEndDate,
              winningRate,
              winningCount,
              totalCount,
              createdAt,
              updatedAt
        );
    }
}
