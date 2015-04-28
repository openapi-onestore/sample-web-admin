package com.skplanet.openapi.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.openapi.dao.BulkJobDAO;
import com.skplanet.openapi.dao.BulkJobDAOImpl;
import com.skplanet.openapi.request.outbound.PayPlanetClient;

@Service("bulkJobService")
public class BulkJobService {

	private static final Logger logger = LoggerFactory.getLogger(BulkJobService.class);
	
	@Autowired
	private BulkJobDAO bulkJobDAO;

	@Autowired
	private PayPlanetClient payPlanetClient;
	
	private List<String> columns = Arrays.asList(new String[] { "mid",
			"billingToken", "appid", "pid", "pName", "orderNo",
			"carrierBillingAmt", "tMembershipAmt", "creditCardAmt" });
	
	public String requestBulkJob(Map<String, String> param) {
		String result = null;
		
		try {
			
			BulkJobInfo bulkJobInfo = makeBulkFile(param);
			
			result = payPlanetClient.createBulkPayment(bulkJobInfo.getProcessingCount(),
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
	
	private BulkJobInfo makeBulkFile(Map<String, String> param)
			throws FileNotFoundException, IOException {
		File tmpFile = new File("D:/samplefolder/bulkfile/" + UUID.randomUUID().toString()
				+ ".bulk");
		Writer writer = new PrintWriter(tmpFile);
		List<Map<String, String>> bulkJobs = bulkJobDAO.selectBulkJob(param);
		logger.debug("bulkjob size : " + bulkJobs.size() + " bulkjob obj " + bulkJobs.get(0).get("appid"));	
		
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
		}

		writer.close();

		return new BulkJobInfo(tmpFile.getAbsolutePath(), processingCount);
	}

	class BulkJobInfo {
		String filePath;
		int processingCount;

		public BulkJobInfo(String filePath, int processingCount) {
			super();
			this.filePath = filePath;
			this.processingCount = processingCount;
		}

		public String getFilePath() {
			return filePath;
		}

		public int getProcessingCount() {
			return processingCount;
		}

	}

}
