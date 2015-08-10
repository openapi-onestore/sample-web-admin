package com.skplanet.openapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.openapi.service.PaymentService;
import com.skplanet.openapi.vo.transaction.TransactionInfo;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	final static Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private PaymentService paymentService;	
	
	@RequestMapping(value="/main", method=RequestMethod.GET)
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
	public ModelAndView requestAdminPaymentResult(@PathVariable String jobId) {
		System.out.println(jobId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin_result");
		
		if (jobId == null) {
			jobId = "You don't transmit some file";
			return modelAndView;
		}
		
		modelAndView.addObject("jobid", jobId);
		
		return modelAndView;
	}

	@RequestMapping(value="/result/file/{jobId}", method=RequestMethod.GET)
	public ModelAndView requestAdminPaymentResultFile(@PathVariable String jobId) {
		System.out.println(jobId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin_result");
		
		if (jobId == null) {
			jobId = "You don't transmit some file";
			return modelAndView;
		}
		
		modelAndView.addObject("jobid", jobId);
		
		return modelAndView;
	}

	@RequestMapping(value="/transaction/{tid}", method=RequestMethod.GET)
	public ModelAndView requestAdminPaymentTransactionInfo(@PathVariable String tid) {
		System.out.println(tid);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin_tid_info");
		
		if (tid == null) {
			tid = "You don't transmit some file";
			return modelAndView;
		}
				
		Map<String, String> param = new HashMap<String, String>();
		param.put("tid", tid);
		
		TransactionInfo transactionInfo = paymentService.requestTidInformationObject(param);
		
		if (transactionInfo != null) {
			modelAndView.addObject("resultCode", transactionInfo.getResultCode());
			modelAndView.addObject("resultMsg", transactionInfo.getResultMsg());
			modelAndView.addObject("mdn", transactionInfo.getPayer().getMdn());
			modelAndView.addObject("carrier", transactionInfo.getPayer().getCarrier());
			modelAndView.addObject("status", transactionInfo.getPaymentTransactionInfo().getStatus());
			modelAndView.addObject("reason", transactionInfo.getPaymentTransactionInfo().getReason());
			modelAndView.addObject("message", transactionInfo.getPaymentTransactionInfo().getMessage());
			modelAndView.addObject("latestUpdate", transactionInfo.getPaymentTransactionInfo().getLastestUpdate());
			modelAndView.addObject("tid", transactionInfo.getPaymentTransactionInfo().getTid());
			modelAndView.addObject("amount", transactionInfo.getPaymentTransactionInfo().getAmount());
			modelAndView.addObject("description", transactionInfo.getPaymentTransactionInfo().getDescription());
			modelAndView.addObject("goods",transactionInfo.getPaymentTransactionInfo().getGoods());
			modelAndView.addObject("paymentMethods",transactionInfo.getPaymentTransactionInfo().getPaymentMethods());
		}
		
		return modelAndView;
	}	
	
}
