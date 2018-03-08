package jp.ijufumi.sample.twitter.domain.dao;

import jp.ijufumi.sample.twitter.domain.entity.TCampaignResult;
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
    List<TCampaignResult> selectAll(int campaignId);

    @Select
    Optional<TCampaignResult> selectById(int campaignId, long twitterId);

    @Transactional
    @Insert
    Result<TCampaignResult> insert(TCampaignResult result);

    @Transactional
    @Update
    Result<TCampaignResult> update(TCampaignResult result);
}
