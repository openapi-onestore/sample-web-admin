package com.skplanet.openapi.request.inbound;

import org.springframework.stereotype.Component;

@Component("RequestHandler")
public class DefaultDelimeterMapRequestHandler extends AbsDelimiterMapRequestHandler {

	@Override
	String getParamDelimiter() {
		return "&";
	}

	@Override
	String getPairDelimiter() {
		return "=";
	}
}
