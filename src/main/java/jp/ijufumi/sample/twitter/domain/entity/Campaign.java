package jp.ijufumi.sample.twitter.domain.entity;

import lombok.Value;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

import java.time.LocalDateTime;

@Entity(immutable = true)
@Value
public class Campaign {
    @Id
    long campaignId;

    String campaignKey;
    String campaignName;
    String bannerImagePath;
    String templateFile;
    LocalDateTime validStartDate;
    LocalDateTime validEndDate;
    LocalDateTime createdAt;
}
