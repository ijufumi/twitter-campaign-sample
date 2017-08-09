SELECT
  /*%expand*/*
FROM
  t_campaign
WHERE
  campaign_key = /*campaignKey*/0 AND
  '2017-08-01 00:00:00'/*systemDate*/ BETWEEN valid_start_date AND valid_end_date
