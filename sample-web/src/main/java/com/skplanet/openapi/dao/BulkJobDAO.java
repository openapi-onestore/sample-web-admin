package com.skplanet.openapi.dao;

import java.util.List;
import java.util.Map;

public interface BulkJobDAO {

	public List<Map<String, String>> selectBulkJob(Map<String, String> param);
	
}
