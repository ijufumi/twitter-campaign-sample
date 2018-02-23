package jp.ijufumi.sample.twitter.domain.entity;

import jp.ijufumi.sample.twitter.domain.value.PrizeStatusObject;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

import java.time.LocalDateTime;

@Entity(immutable = true)
public class TCampaignResult {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private final long resultId;

    private final int campaignId;
    private final long twitterId;
    private final PrizeStatusObject prizeStatus;
    private final String emailAddress;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TCampaignResult(long resultId, int campaignId, long twitterId, PrizeStatusObject prizeStatus, String emailAddress, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.resultId = resultId;
        this.campaignId = campaignId;
        this.twitterId = twitterId;
        this.prizeStatus = prizeStatus;
        this.emailAddress = emailAddress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getResultId() {
        return resultId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public long getTwitterId() {
        return twitterId;
    }

    public PrizeStatusObject getPrizeStatus() {
        return prizeStatus;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public TCampaignResult copyOf(String emailAddress, LocalDateTime updatedAt) {
        return new TCampaignResult(
                resultId,
                campaignId,
                twitterId,
                prizeStatus,
                emailAddress,
                createdAt,
                updatedAt
        );
    }
}
