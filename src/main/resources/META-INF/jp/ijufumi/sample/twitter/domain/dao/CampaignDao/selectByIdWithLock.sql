SELECT
  /*%expand*/*
FROM
  t_campaign
WHERE
  campaign_id = /*campaignId*/0
FOR UPDATE