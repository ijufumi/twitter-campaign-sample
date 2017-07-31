package jp.ijufumi.sample.twitter.domain.entity;

import jp.ijufumi.sample.twitter.domain.value.PrizeStatusObject;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

import java.time.LocalDateTime;

@Entity(immutable = true)
@Value
public class CampaignResult {
    @Id
    long twitterId;

    PrizeStatusObject prizeStatus;
    String emailAddress;
    String accessKey;
    LocalDateTime createdAt;
}
