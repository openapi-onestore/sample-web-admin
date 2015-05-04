CREATE TABLE bulk_job_table (
  `mid` varchar(45) NOT NULL,
  `billing_token` varchar(45) DEFAULT NULL,
  `app_id` varchar(45) DEFAULT NULL,
  `p_id` varchar(45) DEFAULT NULL,
  `p_name` varchar(45) DEFAULT NULL,
  `order_no` varchar(45) DEFAULT NULL,
  `carrier_billing_amt` varchar(45) DEFAULT NULL,
  `t_membership_amt` varchar(45) DEFAULT NULL,
  `credit_card_amt` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`mid`)
);
