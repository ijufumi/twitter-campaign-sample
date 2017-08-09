SELECT
  /*%expand*/*
FROM
  t_campaign
WHERE
  '2017-08-01 00:00:00'/*systemDate*/ BETWEEN valid_start_date AND valid_end_date
ORDER BY
  valid_end_date DESC ,
  valid_start_date DESC