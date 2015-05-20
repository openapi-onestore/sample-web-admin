package com.skplanet.openapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/admin")
public class AdminController {

	@RequestMapping(value="/test", method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView requestAdmin() {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin_index");		
		
		return modelAndView;
	}
	
}
