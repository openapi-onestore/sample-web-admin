package com.skplanet.openapi.test.oauth;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skplanet.openapi.external.oauth.OAuthAccessToken;
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
		oauthClientInfo.setClientId("84xK38rx9iCrFRJVOynsRA0MT0o3LTs83OqDLEJf5g0=");
		oauthClientInfo.setClientSecret("GS1qrhoHMJWpmS6QwLNaG5NcFWFqzh5TrmY5476a2nA=");
		oauthClientInfo.setGrantType("client_credentials");
	}
	
	@Test
	public void getAccessToken() {
		
		OAuthManager oauthManager = new OAuthManager();
		oauthManager.setClientInfo(oauthClientInfo);
		OAuthAccessToken oauth = null;
		
		try {
			Assert.assertEquals(true, oauthManager.createOAuthAccessToken());
			oauth = oauthManager.getOAuthToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertEquals(32, oauth.getAccessToken().length());
		Assert.assertEquals("43200", oauth.getExpired());
		Assert.assertEquals("Bearer", oauth.getTokenType());
		
		String scope = oauth.getScope();
		Assert.assertEquals(true, scope.contains("GetTxInfo"));
		Assert.assertEquals(true, scope.contains("GetTxInfoV2"));
	}
	
	@Test
	public void getAccessTokenValidation() {
		
		OAuthManager oauthManager = new OAuthManager();
		oauthManager.setClientInfo(oauthClientInfo);
		
		OAuthAccessToken oauth = null;
		OAuthVerifyResult oauthVerifyResult = null;
		
		try {
			Assert.assertEquals(true, oauthManager.createOAuthAccessToken());
			oauth = oauthManager.getOAuthToken();
			Assert.assertEquals(true, oauthManager.verifyOAuthToken());
			oauthVerifyResult = oauthManager.getOAuthVerifyResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertEquals("Wed May 20 11:23:54 GMT+09:00 2015", oauthVerifyResult.getExpiredAt());
		Assert.assertEquals("GooPang", oauthVerifyResult.getMerchantId());
		Assert.assertEquals(true, oauthVerifyResult.getMessage().startsWith("NA"));
		Assert.assertEquals("NA", oauthVerifyResult.getReason());
		Assert.assertEquals("SUCCESS", oauthVerifyResult.getStatus());
		Assert.assertEquals(3, oauthVerifyResult.getScopes().length);
	}
	
}
