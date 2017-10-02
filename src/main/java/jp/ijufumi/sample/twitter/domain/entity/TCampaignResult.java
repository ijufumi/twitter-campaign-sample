package jp.ijufumi.sample.twitter.domain.entity;

import jp.ijufumi.sample.twitter.domain.value.PrizeStatusObject;
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
public class TCampaignResult {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long resultId;
    
    int campaignId;
    long twitterId;
    PrizeStatusObject prizeStatus;
    String emailAddress;
    String accessKey;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public TCampaignResult copyOf(String emailAddress, LocalDateTime updatedAt) {
        return new TCampaignResult(
                resultId,
                campaignId,
                twitterId,
                prizeStatus,
                emailAddress,
                accessKey,
                createdAt,
                updatedAt
        );
    }
}
