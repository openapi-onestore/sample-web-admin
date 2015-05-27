package com.skplanet.openapi.test.client;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skplanet.openapi.request.outbound.PayplanetClient;
import com.skplanet.openapi.vo.ClientInfo;
import com.skplanet.openapi.vo.OAuth;
import com.skplanet.openapi.vo.OAuthVerifyResult;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/web.xml",
		"file:src/main/resources/spring/mybatis-context.xml",
		"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"
		})
public class PayplanetClientTest {
	
	@Autowired
	private PayplanetClient payPlanetClient;

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
	
	@Test
	public void getAccessTokenValidation() {
		OAuth oauth = payPlanetClient.createOAuthToken(clientInfo);
		OAuthVerifyResult oAuthVerifyResult = payPlanetClient.verifyOAuthToken(oauth);
		
		Assert.assertEquals(oauth.getAppId(), oAuthVerifyResult.getAppId());
		Assert.assertEquals("Wed May 20 11:23:54 GMT+09:00 2015", oAuthVerifyResult.getExpiredAt());
		Assert.assertEquals("GooPang", oAuthVerifyResult.getMerchantId());
		Assert.assertEquals(true, oAuthVerifyResult.getMessage().startsWith("NA"));
		Assert.assertEquals("NA", oAuthVerifyResult.getReason());
		Assert.assertEquals("SUCCESS", oAuthVerifyResult.getStatus());
		Assert.assertEquals(3, oAuthVerifyResult.getScopes().length);
	}
	
}
