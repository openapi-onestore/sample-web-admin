package com.skplanet.openapi.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BulkJobDAOImpl extends SqlSessionDaoSupport implements BulkJobDAO {

	private static final Logger logger = LoggerFactory.getLogger(BulkJobDAOImpl.class);
	
	public List<Map<String, String>> selectBulkJob(HashMap<String, String> param) {
		
		List<Map<String,String>> result = null;
		
		try {
			result = getSqlSession().selectOne("bulkJob.selectBulkJob", param);
		}catch(Exception e){
			logger.error("fail to load bulk job");
		}
	
		return result;
	}
}
