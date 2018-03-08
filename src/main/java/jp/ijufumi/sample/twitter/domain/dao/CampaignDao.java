package jp.ijufumi.sample.twitter.domain.dao;

import jp.ijufumi.sample.twitter.domain.entity.TCampaign;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ConfigAutowireable
@Dao
public interface CampaignDao {
    @Select
    List<TCampaign> selectAll();

    @Select
    List<TCampaign> selectValidList(LocalDateTime systemDate);

    @Select
    Optional<TCampaign> selectById(int campaignId);

    @Select
    Optional<TCampaign> selectByIdWithLock(int campaignId);

    @Select
    Optional<TCampaign> selectByCampaignKey(String campaignKey, LocalDateTime systemDate);

    @Transactional
    @Update
    Result<TCampaign> update(TCampaign campaign);
}
