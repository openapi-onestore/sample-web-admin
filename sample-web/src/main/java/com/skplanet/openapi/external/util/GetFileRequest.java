package com.skplanet.openapi.external.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class GetFileRequest extends HttpRequest<String> implements Callable<File> {

	private String fileWritePath = null;
	
	@Override
	public File call() throws Exception {
		String path = fileWritePath + getFileName();
		File resultFile = new File(path);
		
		if (!validationUrl()) {
			return null;
		}
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(callUrl);
		
		if (!validationHeader()) {
			addHeaders(httpGet);
		}
		
		CloseableHttpResponse response = httpClient.execute(httpGet);
		
		try {
			statusLine = response.getStatusLine();
			HttpEntity httpEntity = response.getEntity();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), Charset.forName("UTF-8")));
			FileWriter fileWriter = new FileWriter(resultFile);
			
			String buffer = null;
			while( (buffer=bufferedReader.readLine()) != null) {
				System.out.println("File read and write!!");
				fileWriter.append(buffer).append("\n");
				fileWriter.flush();
			}
			fileWriter.close();
			bufferedReader.close();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			response.close();
			httpClient.close();
		}
		
		return resultFile;
	}

	
	@Override
	public void setParameter(String filePath) {
		
	}

	private String getFileName() {
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyyMMdd-HHmmss", Locale.KOREA );
		Date currentTime = new Date ( );
		String dTime = formatter.format ( currentTime );
		
		return dTime + UUID.randomUUID().toString();
	}
	
}
