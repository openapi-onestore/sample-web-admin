package com.skplanet.openapi.test.client;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skplanet.openapi.request.outbound.PayPlanetClient;
import com.skplanet.openapi.vo.ClientInfo;
import com.skplanet.openapi.vo.OAuth;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/web.xml",
		"file:src/main/resources/spring/mybatis-context.xml",
		"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"
		})
public class PayplanetClientTest {
	
	@Autowired
	private PayPlanetClient payPlanetClient;

	@Autowired
	private ClientInfo clientInfo;
	
	@Test
	public void getAccessToken() {
		
		OAuth oauth = payPlanetClient.createOAuthToken(clientInfo);

		Assert.assertEquals("dY4OkOwkhP5eA/lcQhpukZeDvtLUWwCXT9pm4seont1IpFHgf7EMScwN40x6g6FGiap7JXMsvdmZ", oauth.getAccessToken());
		Assert.assertEquals("NA", oauth.getRefreshToken());
		Assert.assertEquals("OA00001234", oauth.getAppId());
		Assert.assertEquals("20150520112354", oauth.getExpired());
		Assert.assertEquals("Bearer", oauth.getTokenType());
		
		String[] scope = oauth.getScope();
		Assert.assertEquals(2, scope.length);
		
		Assert.assertEquals("http://www.payplanet.com/openapi/v1/iap/paymentTransactionDetail", scope[0]);
		Assert.assertEquals("http://www.payplanet.com/openapi/v1/iap/createPaymentTransaction", scope[1]);
		
	}
	
}
