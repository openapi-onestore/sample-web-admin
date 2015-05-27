package com.skplanet.openapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.openapi.service.FileUploadService;
import com.skplanet.openapi.util.HttpClient;


@Controller
@RequestMapping("/bulkjob")
public class OpenApiDummyController {

	@Autowired
	private FileUploadService fileUploadService;
	
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@ResponseBody
	public String requestBulkJobFileUpload(
			@RequestParam("file") MultipartFile request,
			HttpServletRequest servletRequest) {
		
		HttpClient httpClient = new HttpClient();
		String noti = servletRequest.getHeader("notiUrl");
		
		try {
			System.out.println(noti);
			httpClient.get(noti);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		boolean result = fileUploadService.fileUpload(request);
		
		return "result=" + result + "&jobid=002311134";
	}
	
	@RequestMapping(value = "/result", method = RequestMethod.GET)
	@ResponseBody
	public String requestResultFile(String jobid) {
		
		return "jobid=0012334&status=processing";
	}
	
}
