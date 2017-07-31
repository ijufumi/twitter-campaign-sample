CREATE TABLE IF NOT EXISTS campaign_result (
  twitter_id bigint not null PRIMARY KEY,
  prize_status int not null,
  email_address varchar(255) not null,
  access_key varchar(20) not null,
  created_at datetime not null
);

CREATE INDEX campaign_result_IX1 on campaign_result(email_address);