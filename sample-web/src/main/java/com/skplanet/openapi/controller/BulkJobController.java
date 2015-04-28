package com.skplanet.openapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.openapi.service.BulkJobService;

@Controller
@RequestMapping("/payment")
public class BulkJobController {
	
	@Autowired
	private BulkJobService bulkJobService;

	@RequestMapping(value="/noti_listener",method=RequestMethod.POST)
	@ResponseBody
	public String requestBulkJob(String data) { 
		
		
		return "Noti_listener!!";
	}
	
	@RequestMapping(value="/bulkjob",method=RequestMethod.GET)
	@ResponseBody
	public String requestBulkJobUploadPage(String data) { 
		
		String result = bulkJobService.requestBulkJob(new HashMap<String, String>());
		
		//ModelAndView modelAndView = new ModelAndView();
		//modelAndView.setViewName("fileupload_submit");
		
		return result;
	}
	
	@RequestMapping(value="/bulkjob",method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView requestBulkJobFileUpload(MultipartHttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		
		return modelAndView;
	}
	
}
