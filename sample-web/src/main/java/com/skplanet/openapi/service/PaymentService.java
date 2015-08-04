package com.skplanet.openapi.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
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
import com.skplanet.openapi.request.outbound.PayplanetClient;
import com.skplanet.openapi.vo.BulkJobInfo;
import com.skplanet.openapi.vo.NotificationResult;

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
			"billingToken", "pid", "pName", "orderNo", "amtReqPurchase",
			"amtCarrier", "amtCreditCard", "amtTms" });

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
			int processCount = countLines(tmpFile.getAbsolutePath());
			BulkJobInfo bulkJobInfo = new BulkJobInfo(
					tmpFile.getAbsolutePath(), processCount);
			result = payplanetClient.createBulkPayment(bulkJobInfo.getProcessingCount(),
					bulkJobInfo.getFilePath());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String requestBulkJobRequest(String params) {
		String result = null;

		Map<String, String> param = new HashMap<String, String>();

		String[] temp = params.split("&");

		for (String temps : temp) {
			String[] kvp = temps.split("=");
			param.put(kvp[0].toLowerCase(), kvp[1]);
			logger.debug(kvp[0] + " " + kvp[1]);
		}

		try {
			payplanetClient.insertBulkPaymentRequest(param);
			result = "result=SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			result = "result=FAIL&reason=" + e.getMessage();
		}

		return result;
	}

	public List<Map<String, String>> requestBulkJobRequestList() {
		List<Map<String, String>> result = null;
		try {
			result = payplanetClient.selectBulkJobRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public NotificationResult requestNotificationResult(Map<String, String> param) {
		NotificationResult notificationResult = null;
		
		try {
			notificationResult = payplanetClient.selectNotificationResult(param);
		} catch (Exception e) {
			e.printStackTrace();
			return notificationResult;
		}
		return notificationResult;
	}
	
	/**
	 * requestBulkJobResultFile
	 * @param param map
	 * @return String
	 * TODO::get File method
	 */
	public String requestBulkJobResultFile(Map<String, String> param) {
		String result = null;
		
		try {
			result = payplanetClient.getBulkJobResultFile(param);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
			result = "result=FAIL&reason="+e.getMessage();
		}
		return result;
	}
	
	public String requestTidInformation(Map<String, String> param) {
		String result = null;
		
		try {
			result = payplanetClient.getTidInformation(param);
		} catch (Exception e) {
			e.printStackTrace();
			result = "result=FAIL&reason="+e.getMessage();
		}
		return result;
	}
	
	public String requestRefund(Map<String, String> param) {
		String result = null;
		
		try {
			result = payplanetClient.getRefundInformation(param);
		} catch (Exception e) {
			e.printStackTrace();
			result = "result=FAIL&reason="+e.getMessage();
		}
		return result;		
	}
	
	private BulkJobInfo makeBulkFile(Map<String, String> param)
			throws FileNotFoundException, IOException {
		File tmpFile = new File(getBulkfileFormat());
		Writer writer = new PrintWriter(tmpFile);
		
		List<Map<String, String>> bulkJobs = bulkJobDAO.selectBulkJob(param);
		logger.debug("bulkjob size : " + bulkJobs.size());

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
				logger.debug("Writer : " + wr + " Size : " + bulkJob.size());
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
		return String.format(Locale.getDefault(), "%s/%s.bulk",
				localSavingFolder, UUID.randomUUID().toString());
	}

	private int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}
	
	

}
