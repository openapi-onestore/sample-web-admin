package com.skplanet.openapi.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BulkJobDAOImpl extends SqlSessionDaoSupport implements BulkJobDAO {

	private static final Logger logger = LoggerFactory.getLogger(BulkJobDAOImpl.class);
	
	@Resource(name="sqlSessionFactory")
	public void setSuperSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}
	
	@Resource(name="sqlSessionTemplate")
	public void setSuperSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	public List<Map<String, String>> selectBulkJob(Map<String, String> param) {
		
		List<Map<String,String>> result = null;
		
		try {
			result = getSqlSession().selectList("bulkJob.selectBulkJob", param);
		}catch(Exception e){
			logger.error("fail to load bulk job, error message : " + e.getMessage());
		}
		
		return result;
	}
}
