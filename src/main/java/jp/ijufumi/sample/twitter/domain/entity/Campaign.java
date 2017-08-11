package jp.ijufumi.sample.twitter.domain.entity;

import lombok.Builder;
import lombok.Value;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

import java.time.LocalDateTime;

@Entity(immutable = true)
@Value
@Builder
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int campaignId;

    String campaignKey;
    String campaignName;
    String bannerImagePath;
    String templateFile;
    LocalDateTime validStartDate;
    LocalDateTime validEndDate;
    int winningRate;
    int winningCount;
    int totalCount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public Campaign copyOf(int winningCount, int totalCount, LocalDateTime updatedAt) {
        return new Campaign(
              campaignId,
              campaignKey,
              campaignName,
              bannerImagePath,
              templateFile,
              validStartDate,
              validEndDate,
              winningRate,
              winningCount,
              totalCount,
              createdAt,
              updatedAt
        );
    }
}
