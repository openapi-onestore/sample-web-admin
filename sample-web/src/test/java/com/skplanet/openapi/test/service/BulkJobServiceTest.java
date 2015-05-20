package com.skplanet.openapi.test.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skplanet.openapi.service.PaymentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/web.xml",
		"file:src/main/resources/spring/mybatis-context.xml",
		"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"
		})
public class BulkJobServiceTest {

	@Autowired
	PaymentService bulkJobService;
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void requestBulkJobTest() {
		
		Map<String,String> hashMap = new HashMap<String, String>();
		hashMap.put("START_NO", "1");
		hashMap.put("END_NO", "2");
		
		String result = bulkJobService.requestBulkJob(hashMap);
		
		Assert.assertEquals("result=true", result);
		
	}
	
}
