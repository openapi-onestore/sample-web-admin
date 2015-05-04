package com.skplanet.openapi.test.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skplanet.openapi.dao.BulkJobDAO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/web.xml",
		"file:src/main/resources/spring/mybatis-context.xml",
		"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"
		})
public class BulkJobDaoTest {
	
	@Autowired
	private BulkJobDAO bulkJobDAO;
	
	@Test
	public void getBulkJobDao() {
		Assert.assertEquals(1, 1);
		
		Map<String, String> hmap = new HashMap<String, String>();
		hmap.put("START_NO", "1");
		hmap.put("END_NO", "2");
		
		List<Map<String,String>> map = bulkJobDAO.selectBulkJob(hmap);
		
		System.out.println(map.get(0).get("mid"));
	}
	
	
}
