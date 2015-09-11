package com.skplanet.openapi.external.bulkpay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.skplanet.openapi.external.bulkpay.OpenApiException.OpenApi;

public class OpenApiHttpClient {
	private StatusLine statusLine;
	private Map<String, String> headers = null;

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	private void addHeaders(AbstractHttpMessage httpMessage) {
		for (String key : headers.keySet()) {
			httpMessage.setHeader(key, headers.get(key));
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
	
	public File getFile(String url, String fileWritePath) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		addHeaders(httpGet);
		
		File file = null;
		CloseableHttpResponse response = httpclient.execute(httpGet);		
		
		try {
			setStatusLine(response.getStatusLine());
			HttpEntity httpEntity = response.getEntity();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), Charset.forName("UTF-8")));
			file = new File(fileWritePath);
			FileWriter fileWriter = new FileWriter(file);
			
			String buffer = null;
			while( (buffer=bufferedReader.readLine()) != null) {
				fileWriter.append(buffer);
				fileWriter.flush();
			}
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			new OpenApiException(OpenApi.OPENAPI_JOB_EXECUTE_ERROR, e.getMessage());
		} finally {
			response.close();
		}
		return file;
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

	public String post(String url, String data) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		addHeaders(httpPost);

		HttpEntity httpEntity = new StringEntity(data,
				ContentType.APPLICATION_JSON);
		httpPost.setEntity(httpEntity);
		CloseableHttpResponse response = httpclient.execute(httpPost);

		try {
			setStatusLine(response.getStatusLine());
			return EntityUtils.toString(response.getEntity());
		} finally {
			response.close();
		}

	}

	public String postChunkedString(String uri, File sendFile)
			throws Exception {
		System.out.println("Post Chunked String");
				
		if (sendFile == null || !sendFile.canRead()) {
			return "fail";
		}
		
		setStatusLine(null);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(uri);
			addHeaders(httpPost);
			
			// TODO plain/text로 처리 가능한지 확인 필
			InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(sendFile), sendFile.length(), ContentType.TEXT_PLAIN);
			reqEntity.setChunked(true);
			
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
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
