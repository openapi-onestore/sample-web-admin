package com.skplanet.openapi.test.oauth;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skplanet.openapi.external.oauth.OAuth;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManager;
import com.skplanet.openapi.external.oauth.OAuthVerifyResult;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/web.xml",
		"file:src/main/resources/spring/mybatis-context.xml",
		"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"
		})
public class OAuthManagerTest {
	
	private OAuthClientInfo oauthClientInfo;
	
	@Before
	public void setUp() {
		oauthClientInfo = new OAuthClientInfo();
		oauthClientInfo.setClientId("9z/fmk5hAC7fcXWjbPq4Tlh3dgYmafZBKfrGOpIIYeqlfqVoTfyxgGoe/NU2Ycp7");
		oauthClientInfo.setClientSecret("wJakRyD02F+c7LGiYseIG5PxRAK14IuT8DBfLFycX48=");
		oauthClientInfo.setGrantType("client_credentials");
	}
	
	@Test
	public void getAccessToken() {
	
		OAuthManager oauthManager = new OAuthManager();
		oauthManager.setClientInfo(oauthClientInfo);
		OAuth oauth = null;
		
		try {
			Assert.assertEquals(true, oauthManager.createOAuthToken());
			oauth = oauthManager.getOAuthToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
		
		OAuthManager oauthManager = new OAuthManager();
		oauthManager.setClientInfo(oauthClientInfo);
		
		OAuth oauth = null;
		OAuthVerifyResult oauthVerifyResult = null;
		
		try {
			Assert.assertEquals(true, oauthManager.createOAuthToken());
			oauth = oauthManager.getOAuthToken();
			Assert.assertEquals(true, oauthManager.verifyOAuthToken());
			oauthVerifyResult = oauthManager.getOAuthVerifyResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertEquals(oauth.getAppId(), oauthVerifyResult.getAppId());
		Assert.assertEquals("Wed May 20 11:23:54 GMT+09:00 2015", oauthVerifyResult.getExpiredAt());
		Assert.assertEquals("GooPang", oauthVerifyResult.getMerchantId());
		Assert.assertEquals(true, oauthVerifyResult.getMessage().startsWith("NA"));
		Assert.assertEquals("NA", oauthVerifyResult.getReason());
		Assert.assertEquals("SUCCESS", oauthVerifyResult.getStatus());
		Assert.assertEquals(3, oauthVerifyResult.getScopes().length);
	}
	
}
