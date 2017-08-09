package jp.ijufumi.sample.twitter.domain.dao;

import jp.ijufumi.sample.twitter.domain.entity.Campaign;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.time.LocalDateTime;
import java.util.List;

@ConfigAutowireable
@Dao
public interface CampaignDao {
    @Select
    List<Campaign> selectAll();

    @Select
    List<Campaign> selectValidList(LocalDateTime systemDate);

    @Select
    Campaign selectById(long campaignId);

    @Select
    Campaign selectByCampaignKey(String campaignKey, LocalDateTime systemDate);
}
