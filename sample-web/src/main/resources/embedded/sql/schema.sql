CREATE TABLE bulk_job_table (
  `mid` varchar(45) NOT NULL,
  `billing_token` varchar(45) DEFAULT NULL,
  `product_id` varchar(45) DEFAULT NULL,
  `product_name` varchar(45) DEFAULT NULL,
  `order_no` varchar(45) DEFAULT NULL,
  `amt_request_purchase` varchar(45) DEFAULT NULL,
  `amt_carrier` varchar(45) DEFAULT NULL,
  `amt_credit_card` varchar(45) DEFAULT NULL,
  `amt_tms` varchar(45) DEFAULT NULL,  
  PRIMARY KEY (`mid`)
);

CREATE TABLE bulk_job_request (
  `mid` bigint NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `reason` varchar(45) DEFAULT NULL,
  `waiting_jobs` varchar(45) DEFAULT NULL,
  `job_id` varchar(45) DEFAULT NULL,
  `upload_file` varchar(45) DEFAULT NULL,
  `upload_date` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`mid`)
);

ALTER TABLE bulk_job_request ALTER COLUMN mid bigint AUTO_INCREMENT;
