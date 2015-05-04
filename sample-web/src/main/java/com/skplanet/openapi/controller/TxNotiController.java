package com.skplanet.openapi.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.openapi.service.TxNotiService;
import com.skplanet.openapi.request.inbound.InBoundRequestHandler;

@Controller
@RequestMapping("/notification")
public class TxNotiController {

	final static Logger logger = LoggerFactory.getLogger(TxNotiController.class);

	@Autowired
	private TxNotiService txNotiService;

	@Autowired
	private InBoundRequestHandler<Map<String, String>> requestHandler;

	@RequestMapping(value = "/noti_listener", method = RequestMethod.POST)
	@ResponseBody
	public String notiListen(@RequestParam Map<String,String> data) {
		System.out.println(data.toString());
		String result = null;
		try {
			// TODO request param parse
			result = txNotiService.processNoti(data);
			// TODO 200 OK
			return result;
		} catch (Exception e) {
			// TODO Fail Response
			return "Exception";
		}
	}
}
