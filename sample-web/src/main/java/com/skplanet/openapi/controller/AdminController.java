package com.skplanet.openapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	final static Logger logger = LoggerFactory.getLogger(AdminController.class);

	@RequestMapping(value="/main", method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView requestAdminMain(@RequestParam(value="uploadResult",required=false) String result) {
		System.out.println(result);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin_index");
		
		if (result == null) {
			result = "You don't transmit some file";
		}
		modelAndView.addObject("uploadResult", result);
		
		return modelAndView;
	}

	@RequestMapping(value="/result/{jobId}", method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView requestAdminPaymentResult(@PathVariable String jobId) {
		System.out.println(jobId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin_result");
		
		if (jobId == null) {
			jobId = "You don't transmit some file";
			return modelAndView;
		}
		
		
		
		modelAndView.addObject("uploadResult", jobId);
		
		return modelAndView;
	}

	@RequestMapping(value="/result/file/{jobId}", method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView requestAdminPaymentResultFile(@PathVariable String jobId) {
		System.out.println(jobId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin_result");
		
		if (jobId == null) {
			jobId = "You don't transmit some file";
			return modelAndView;
		}
		
		
		
		modelAndView.addObject("uploadResult", jobId);
		
		return modelAndView;
	}

	
}
