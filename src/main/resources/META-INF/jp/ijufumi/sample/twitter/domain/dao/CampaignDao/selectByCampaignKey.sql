SELECT
  /*%expand*/*
FROM
  t_campaign
WHERE
  campaign_key = /*campaignKey*/0 AND
  /*systemDate*/'2017-08-01 00:00:00' BETWEEN display_start_date AND display_end_date
