package com.skplanet.openapi.test.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skplanet.openapi.dao.FilePaymentDAO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/web.xml",
		"file:src/main/resources/spring/mybatis-context.xml",
		"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"
		})
public class BulkJobDaoTest {
	
	@Autowired
	private FilePaymentDAO filePaymentDAO;
	
	private Map<String, String> hashMap;
	
	@Before
	public void setUp() {
		hashMap = new HashMap<String, String>();
	}
	
	@Test
	public void getBulkJobDaoStandard() {	
		hashMap.clear();
		hashMap.put("START_NO", "1");
		hashMap.put("END_NO", "2");
		
		List<Map<String,String>> map = filePaymentDAO.selectFilePayment(hashMap);
		
		Assert.assertEquals(map.size(), 2);
		
		Assert.assertEquals("1", map.get(0).get("mid"));
		Assert.assertEquals("AABBCCDD", map.get(0).get("billingToken"));
		Assert.assertEquals("0901208001", map.get(0).get("pid"));
		Assert.assertEquals("Health portion 1", map.get(0).get("pName"));
		Assert.assertEquals("11125521", map.get(0).get("orderNo"));
		Assert.assertEquals("5000", map.get(0).get("amtReqPurchase"));
		Assert.assertEquals("1000", map.get(0).get("amtCarrier"));
		Assert.assertEquals("4000", map.get(0).get("amtCreditCard"));
		Assert.assertEquals("0", map.get(0).get("amtTms"));
		
		Assert.assertEquals("2", map.get(1).get("mid"));
		Assert.assertEquals("AABBCCDD", map.get(1).get("billingToken"));
		Assert.assertEquals("0901208002", map.get(1).get("pid"));
		Assert.assertEquals("Health portion 2", map.get(1).get("pName"));
		Assert.assertEquals("11125522", map.get(1).get("orderNo"));
		Assert.assertEquals("1000", map.get(1).get("amtReqPurchase"));
		Assert.assertEquals("1000", map.get(1).get("amtCarrier"));
		Assert.assertEquals("0", map.get(1).get("amtCreditCard"));
		Assert.assertEquals("0", map.get(1).get("amtTms"));
	}
	
	@Test
	public void getBulkJobFail() {
		hashMap.clear();
		hashMap.put("START_NO", "4");
		hashMap.put("END_NO", "5");
		
		List<Map<String,String>> map = filePaymentDAO.selectFilePayment(hashMap);
		
		Assert.assertEquals(map.size(), 0);
	}
	
	@Test
	public void getBulkJobRequest() {
		List<Map<String,String>> map = filePaymentDAO.selectFilePaymentRequest();
		
		Assert.assertEquals(1, map.size());
	}
	
	@Test
	public void insertBulkJobRequest() {
		List<Map<String,String>> map = filePaymentDAO.selectFilePaymentRequest();
		
		Assert.assertEquals(map.size(), 0);
		
		hashMap.clear();
		hashMap.put("status", "SUCCESS");
		hashMap.put("reason", "0000");
		hashMap.put("waiting_jobs", "10");
		hashMap.put("job_id", "001234");
		hashMap.put("upload_file", "bulkjob.txt");
		hashMap.put("upload_date", "20150511");
		
		try {
			filePaymentDAO.insertFilePaymentRequest(hashMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		map = filePaymentDAO.selectFilePaymentRequest();
		
		Assert.assertEquals(1, map.size());
		
		System.out.println(map);
	}
	
}
