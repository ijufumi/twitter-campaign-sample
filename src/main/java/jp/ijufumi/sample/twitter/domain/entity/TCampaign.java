package jp.ijufumi.sample.twitter.domain.entity;

import lombok.Builder;
import lombok.Value;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.jdbc.entity.NamingType;

import java.time.LocalDateTime;

@Entity(immutable = true)
@Value
@Builder
public class TCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int campaignId;

    String campaignKey;
    String campaignName;
    String bannerImagePath;
    String screenName;
    long statusId;
    LocalDateTime validStartDate;
    LocalDateTime validEndDate;
    LocalDateTime displayStartDate;
    LocalDateTime displayEndDate;
    int winningRate;
    int winningCount;
    int totalCount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

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
