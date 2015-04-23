package com.skplanet.openapi.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClient {

	private StatusLine statusLine;
	private List<HttpHeader> headers;
	
	public HttpClient(List<HttpHeader> headers){
		this.headers = headers;
	}
	
	public HttpClient(){
		this(Collections.<HttpHeader>emptyList());
	}
	
	private void addHeaders(AbstractHttpMessage httpMessage){
		for(HttpHeader header : headers){			
			httpMessage.setHeader(header.getName(),header.getValue());
		}
	}
	
	public String get(String url) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		addHeaders(httpGet);
		
		CloseableHttpResponse response = httpclient.execute(httpGet);
		try {
			setStatusLine(response.getStatusLine());
			return EntityUtils.toString(response.getEntity());
		} finally {
			response.close();
		}
	}

	public String post(String url, Map<String, String> data) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		addHeaders(httpPost);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Iterator<String> keys = data.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			nvps.add(new BasicNameValuePair(key, data.get(key)));
		}

		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response = httpclient.execute(httpPost);

		try {
			setStatusLine(response.getStatusLine());
			return EntityUtils.toString(response.getEntity());
		} finally {
			response.close();
		}
	}

	public String postChunkedString(String uri, String filePath)
			throws Exception {

		setStatusLine(null);

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost(uri);
			addHeaders(httpPost);
			
			File file = new File(filePath);

			// TODO plain/text로 처리 가능한지 확인 필
			InputStreamEntity reqEntity = new InputStreamEntity(
					new FileInputStream(file), -1, ContentType.TEXT_PLAIN);
			reqEntity.setChunked(true);
			httppost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				setStatusLine(response.getStatusLine());
				return EntityUtils.toString(response.getEntity());

			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	private void setStatusLine(StatusLine statusLine) {
		this.statusLine = statusLine;
	}

	public String getStatus() {
		if (statusLine != null) {
			return statusLine.toString();
		} else {
			return null;
		}
	}
}
