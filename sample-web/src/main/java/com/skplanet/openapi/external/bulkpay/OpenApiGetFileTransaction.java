package com.skplanet.openapi.external.bulkpay;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

import com.skplanet.openapi.external.bulkpay.OpenApiException.OpenApi;

public class OpenApiGetFileTransaction implements Callable<File> {

	private OpenApiHttpClient httpClient;
	private Map<String, String> paramMap;
	private String callUrl;
	private String fileWritePath;
	
	public OpenApiGetFileTransaction(Map<String, String> header) {
		this.paramMap = header;
	}
	
	@Override
	public File call() throws Exception {
		
		if (fileWritePath == null) {
			throw new OpenApiException(OpenApi.OPENAPI_FILE_NOT_FOUND_ERROR, "{'reason':'File not found.'}");
		}
		
		httpClient = new OpenApiHttpClient();
		httpClient.setHeaders(paramMap);
		
		File resultFile = httpClient.getFile(callUrl, fileWritePath);
		
		return resultFile;
	}
	
	public void setCallUrl(String callingUrl) {
		this.callUrl = callingUrl;
		System.out.println("OpenApiGetTransaction" + this.callUrl);
	}
	
	public void setFileWritePath(String fileWritePath) {
		this.fileWritePath = fileWritePath;
		System.out.println("OpenApiGetTransaction" + this.fileWritePath);
	}
	
}