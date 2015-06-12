package com.skplanet.openapi.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.openapi.dao.BulkJobDAO;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.request.outbound.PayplanetClient;
import com.skplanet.openapi.vo.BulkJobInfo;

@Service("paymentService")
public class PaymentService {

	private static final Logger logger = LoggerFactory
			.getLogger(PaymentService.class);
	
	@Value("${bulkjobservice.localsavefolder}")
	private String localSavingFolder;
	
	@Autowired
	private BulkJobDAO bulkJobDAO;

	@Autowired
	private PayplanetClient payplanetClient;
	
	private List<String> columns = Arrays.asList(new String[] { "mid",
			"billingToken", "pid", "pName", "orderNo",
			"amtReqPurchase", "amtCarrier", "amtCreditCard", "amtTms" });
	
	public String requestBulkJob(Map<String, String> param) {
		String result = null;
		 
		try {
			BulkJobInfo bulkJobInfo = makeBulkFile(param);
			result = payplanetClient
					.createBulkPayment(bulkJobInfo.getProcessingCount(),
							bulkJobInfo.getFilePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public String requestBulkJob(MultipartFile multipartFile) {
		String result = null;
		File tmpFile = new File(getBulkfileFormat());
		
		try {
			multipartFile.transferTo(tmpFile);
			BulkJobInfo bulkJobInfo = new BulkJobInfo(
					tmpFile.getAbsolutePath(), 1);
			result = payplanetClient
					.createBulkPayment(bulkJobInfo.getProcessingCount(),
							bulkJobInfo.getFilePath());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private BulkJobInfo makeBulkFile(Map<String, String> param)
			throws FileNotFoundException, IOException {
		File tmpFile = new File(getBulkfileFormat());
		Writer writer = new PrintWriter(tmpFile);
		
		List<Map<String, String>> bulkJobs = bulkJobDAO.selectBulkJob(param);
		logger.debug("bulkjob size : " + bulkJobs.size() + " bulkjob obj "
				+ bulkJobs.get(0).get("appid"));

		int processingCount = bulkJobs.size();
		
		Iterator<String> iterator = columns.iterator();
		while (iterator.hasNext()) {
			writer.write(iterator.next());
			if (iterator.hasNext()) {
				writer.write(",");
			}
		}
		
		writer.write('\n');
		
		for (Map<String, String> bulkJob : bulkJobs) {
			iterator = columns.iterator();
			while (iterator.hasNext()) {
				String wr = bulkJob.get(iterator.next());
				logger.debug("Writer : " + wr);
				writer.write(wr);
				if (iterator.hasNext()) {
					writer.write(",");
				}
			}
			writer.write('\n');
		}
		writer.close();
		
		return new BulkJobInfo(tmpFile.getAbsolutePath(), processingCount);
	}
	
	private String getBulkfileFormat() {
		return String.format(Locale.getDefault(),
				"%s/%s.bulk", localSavingFolder, UUID.randomUUID().toString());
	}

}
