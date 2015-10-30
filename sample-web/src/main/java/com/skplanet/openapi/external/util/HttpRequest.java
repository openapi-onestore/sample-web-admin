package com.skplanet.openapi.external.util;

import java.util.Map;

import org.apache.http.StatusLine;
import org.apache.http.message.AbstractHttpMessage;

public abstract class HttpRequest<T> {
	
	protected String callUrl = null;
	protected String parameter = null;
	protected Map<String, String> headers = null;
	protected StatusLine statusLine;
		
	abstract public void setParameter(T parameter);
	
	protected void addHeaders(AbstractHttpMessage httpMessage) {
		for (String key : headers.keySet()) {
			System.out.println("Add headers : " + key + " value : " + headers.get(key));
			httpMessage.setHeader(key, headers.get(key));
		}
	}
	
	protected boolean validationUrl() {
		if (callUrl == null) 
			return false;
		return true;
	}
	
	protected boolean validationParameter() {
		if (parameter == null)
			return false;
		return true;
	}
	
	protected boolean validationHeader() {
		if (headers == null)
			return false;
		return true;
	}
	
	protected String getParameter() {
		return this.parameter;
	}
	
	protected String getCallUrl() {
		return this.callUrl;
	}
	
	public void setHeader(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public void setCallUrl(String callUrl) {
		this.callUrl = callUrl;
	}
	
	
	
}
