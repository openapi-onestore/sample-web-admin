package com.skplanet.openapi.external.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Callable;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class FilePostRequest extends HttpRequest<File> implements
		Callable<String> {
	
	private File sendFile = null;
	
	@Override
	public String call() throws Exception {
		String result = null;
		
		// TODO: fail interface
		if (sendFile == null || !sendFile.canRead()) {
			return "fail";
		}
		
		if (!validationUrl()) {
			return "fail - not call Url";
		}
		
		HttpPost httpPost = new HttpPost(callUrl);
		if (!validationHeader()) {
			addHeaders(httpPost);
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(sendFile), sendFile.length(), ContentType.TEXT_PLAIN);
		reqEntity.setChunked(true);
		
		httpPost.setEntity(reqEntity);
		CloseableHttpResponse response = httpclient.execute(httpPost);
		try {
			statusLine = response.getStatusLine();
			result = EntityUtils.toString(response.getEntity());
		} finally {
			response.close();
			httpclient.close();
		}		
		
		return result;
	}

	@Override
	public void setParameter(File sendFile) {
		this.sendFile = sendFile;
	}
	
}
