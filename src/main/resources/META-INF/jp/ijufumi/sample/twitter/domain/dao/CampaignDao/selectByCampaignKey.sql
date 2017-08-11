SELECT
  /*%expand*/*
FROM
  t_campaign
WHERE
  campaign_key = /*campaignKey*/0 AND
  /*systemDate*/'2017-08-01 00:00:00' BETWEEN valid_start_date AND valid_end_date
