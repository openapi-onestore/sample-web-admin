package com.skplanet.openapi.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.openapi.service.PaymentService;
import com.skplanet.openapi.vo.NotificationResult;

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

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> requestBulkJobReqList() {
		
		logger.debug("Request BulkJobRequestList ");		
		List<Map<String, String>> result = paymentService.requestBulkJobRequestList();
		
		return result;
	}
	
	@RequestMapping(value = "/bulkjob", method = RequestMethod.POST)
	public String requestBulkJobUploadFromFile(
			@RequestParam("bulkjob") MultipartFile request) {
		
		String result = paymentService.requestBulkJob(request);
		
		if (result.contains("SUCCESS")) {
			// bulkJob file upload success
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH:mm");
			result += "&upload_file=" + request.getOriginalFilename() + "&upload_date=" + sdf.format(Calendar.getInstance().getTime());
			result = paymentService.requestBulkJobRequest(result);
		} else {
			result = "bulkJobUpload fail. please upload your file";
		}
		
		return "redirect:../admin/test?uploadResult="+result;
	}
	
	@RequestMapping(value = "/result/{jobid}", method=RequestMethod.GET)
	@ResponseBody
	public String requestResultFile(@PathVariable String jobid) {
		
		String result = null;
		
		if (jobid == null) {
			return "Job ID is null, check your job id";
		}
		
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("jobId", jobid);
		
		NotificationResult notificationResult = paymentService.requestNotificationResult(hashMap);
		hashMap.put("verifySign", notificationResult.getVerifySign());
		
		System.out.println(hashMap);
		
		result = paymentService.requestBulkJobResultFile(hashMap);
		
		return result;
	}

	@RequestMapping(value = "/transaction/{tid}", method=RequestMethod.GET)
	@ResponseBody
	public String requestTxidInfo(@PathVariable String tid) {
		
		String result = null;
		
		if (tid == null) {
			return "Job ID is null, check your job id";
		}
		
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("tid", tid);		
		
		result = paymentService.requestTidInformation(hashMap);
		
		return result;
	}
	
	@RequestMapping(value = "/refund/{tid}", method=RequestMethod.GET)
	@ResponseBody
	public String requestCancelTransaction(@PathVariable String tid) {
		
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("tid", tid);
		
		String tidInfo = paymentService.requestTidInformation(hashMap);
		String result = null;
		
		if (tidInfo.startsWith("result=FAIL")) {
			return "Tid information request is failure";
		}
		
		hashMap.clear();
		hashMap.put("tidInfo", tidInfo);
		
		result = paymentService.requestRefund(hashMap);
		
		return result;
	}	
	
}
