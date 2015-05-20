package com.skplanet.openapi.dao;

import com.skplanet.openapi.vo.ClientInfo;
import com.skplanet.openapi.vo.OAuth;

public interface OAuthDAO {

	OAuth get(ClientInfo clientInfo);
	
}
