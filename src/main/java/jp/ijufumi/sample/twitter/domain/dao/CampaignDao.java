package jp.ijufumi.sample.twitter.domain.dao;

import jp.ijufumi.sample.twitter.domain.entity.Campaign;
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
    List<Campaign> selectAll();

    @Select
    List<Campaign> selectValidList(LocalDateTime systemDate);

    @Select
    Optional<Campaign> selectById(int campaignId);

    @Select
    Optional<Campaign> selectByIdWithLock(int campaignId);

    @Select
    Optional<Campaign> selectByCampaignKey(String campaignKey, LocalDateTime systemDate);

    @Transactional
    @Update
    Result<Campaign> update(Campaign campaign);
}
