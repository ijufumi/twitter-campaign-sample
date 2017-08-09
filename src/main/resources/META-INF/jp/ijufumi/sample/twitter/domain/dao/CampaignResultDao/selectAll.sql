SELECT
  /*%expand*/*
FROM
  t_campaign_result
WHERE
  campaign_id = /* campaignId */0
ORDER BY
  created_at