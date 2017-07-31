package jp.ijufumi.sample.twitter.domain.dao;

import jp.ijufumi.sample.twitter.domain.entity.CampaignResult;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configurable
@Dao
public interface CampaignResultDao {
    @Select
    List<CampaignResult> selectAll();

    @Select
    CampaignResult selectById(long twitterId);

    @Transactional
    @Insert
    int insert(CampaignResult result);

    @Transactional
    @Update
    int update(CampaignResult result);
}
