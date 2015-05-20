package com.skplanet.openapi.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.openapi.service.PaymentService;

@Controller
@RequestMapping("/payment")
public class PaymentController {

	private static final Logger logger = LoggerFactory
			.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value = "/bulkjob", method = RequestMethod.GET)
	@ResponseBody
	public String requestBulkJobUploadFromDB(String start_no, String end_no) {
		
		logger.debug("Request param : " + start_no + " " + end_no);
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("START_NO", start_no);
		hashMap.put("END_NO", end_no);
		
		String result = paymentService.requestBulkJob(hashMap);
		
		return result;
	}
	
	@RequestMapping(value = "/bulkjob", method = RequestMethod.POST)
	@ResponseBody
	public String requestBulkJobUploadFromFile(
			@RequestParam("bulkjob") MultipartFile request,
			HttpServletRequest servletRequest) {
		
		String result = paymentService.requestBulkJob(request);
		
		return result;
	}

}