package com.skplanet.openapi.dao;

import java.util.List;
import java.util.Map;

public interface BulkJobDAO {

	List<Map<String, String>> selectBulkJob(Map<String, String> param);
	
	List<Map<String, String>> selectBulkJobRequest();
	void addBulkJobRequest(Map<String, String> param) throws Exception;
	void updateNotifiVerified(Map<String, String> param) throws Exception;
	
}
