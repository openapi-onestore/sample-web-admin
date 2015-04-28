package com.skplanet.openapi.request.outbound;

import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("outRequestHandler")
public abstract class AbsDelimiterStringRequestHandler implements
		OutBoundRequestHandler<Map<String, String>, String> {

	abstract String getParamDelimiter();

	abstract String getPairDelimiter();

	public String handle(Map<String, String> data) {

		StringBuilder sb = new StringBuilder();

        Iterator<String> keys = data.keySet().iterator();
        while( keys.hasNext() ){
            String key = keys.next();
            
            sb.append(key).append(getPairDelimiter()).append(data.get(key));
            
            if(keys.hasNext()) {
            	sb.append(getParamDelimiter());
            }
        }

        return sb.toString();
	}

}
