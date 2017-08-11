use campaign;

CREATE TABLE IF NOT EXISTS t_campaign (
  campaign_id INT NOT NULL PRIMARY KEY,
  campaign_key VARCHAR(100) NOT NULL,
  campaign_name VARCHAR(100) NOT NULL,
  banner_image_path VARCHAR(255) NOT NULL,
  template_file VARCHAR(100) NOT NULL,
  valid_start_date DATETIME NOT NULL,
  valid_end_date DATETIME NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL
);

CREATE INDEX t_campaign_IX1 on t_campaign(campaign_key);
CREATE INDEX t_campaign_IX2 on t_campaign(valid_start_date, valid_end_date);

CREATE TABLE IF NOT EXISTS t_campaign_result (
  result_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  campaign_id INT NOT NULL,
  twitter_id BIGINT not null,
  prize_status INT NOT NULL,
  email_address VARCHAR(255) NOT NULL,
  access_key VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  FOREIGN KEY (campaign_id) REFERENCES t_campaign(campaign_id)
);

CREATE UNIQUE INDEX t_campaign_result_IX1 on t_campaign_result(campaign_id, twitter_id);
CREATE INDEX t_campaign_result_IX2 on t_campaign_result(email_address);

