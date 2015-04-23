package com.skplanet.openapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/payment")
public class BulkJobController {

	@RequestMapping(value="/noti_listener",method=RequestMethod.POST)
	@ResponseBody
	public String requestBulkJob(String data) { 
		
		
		
		
		return "";		
	}	
}
