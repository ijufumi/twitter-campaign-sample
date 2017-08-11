package jp.ijufumi.sample.twitter.domain.dao;

import jp.ijufumi.sample.twitter.domain.entity.CampaignResult;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@ConfigAutowireable
@Dao
public interface CampaignResultDao {
    @Select
    List<CampaignResult> selectAll(int campaignId);

    @Select
    Optional<CampaignResult> selectById(int campaignId, long twitterId);

    @Transactional
    @Insert
    Result<CampaignResult> insert(CampaignResult result);

    @Transactional
    @Update
    Result<CampaignResult> update(CampaignResult result);
}
