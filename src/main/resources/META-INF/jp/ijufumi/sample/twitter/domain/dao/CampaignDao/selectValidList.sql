SELECT
  /*%expand*/*
FROM
  t_campaign
WHERE
  /*systemDate*/'2017-08-01 00:00:00' BETWEEN display_start_date AND display_end_date
ORDER BY
  valid_end_date DESC ,
  valid_start_date DESC