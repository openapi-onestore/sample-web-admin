package com.skplanet.openapi.request.inbound;

import java.util.Collections;
import java.util.Map;

public abstract class AbsDelimiterMapRequestHandler implements
		InBoundRequestHandler<Map<String, String>> {

	abstract String getParamDelimiter();

	abstract String getPairDelimiter();

	public Map<String, String> readValue(String data) throws Exception{

		String params[] = data.split(getParamDelimiter());

		Map<String, String> map = Collections.emptyMap();

		for (String param : params) {
			String[] pair = param.split(getPairDelimiter());
			map.put(pair[0], pair[1]);
		}

		return map;
	}
}
