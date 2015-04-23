package com.skplanet.openapi.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.skplanet.openapi.dao.BulkJobDAO;
import com.skplanet.openapi.request.outbound.PayPlanetClient;

public class BulkJobService {

	@Autowired
	private BulkJobDAO bulkJobDAO;

	@Autowired
	private PayPlanetClient payPlanetClient;

	private List<String> columns = Arrays.asList(new String[] { "mid",
			"billingToken", "appid", "pid", "pName", "orderNo", "totalAmt",
			"carrierBillingAmt", "tMembershipAmt", "creditCardAmt" });

	public String requestBulkJob(Map<String, String> param) {

		try {

			BulkJobInfo buljJobInfo = makeBulkFile(param);

			payPlanetClient.createBulkPayment(buljJobInfo.getProcessingCount(),
					buljJobInfo.getFilePath());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	private BulkJobInfo makeBulkFile(Map<String, String> param)
			throws FileNotFoundException, IOException {
		// TODO DB로 부터 BulkJob을 조회하여 파일에 저장한다.
		File tmpFile = new File("/tmp/" + UUID.randomUUID().toString()
				+ ".bulk");
		Writer writer = new PrintWriter(tmpFile);
		List<Map<String, String>> bulkJobs = bulkJobDAO.selectBulkJob(param);
		int processingCount = bulkJobs.size();

		Iterator<String> iterator = columns.iterator();
		while (iterator.hasNext()) {
			writer.write(iterator.next());
			if (iterator.hasNext()) {
				writer.write(",");
			}
		}

		for (Map<String, String> bulkJob : bulkJobs) {
			iterator = columns.iterator();
			while (iterator.hasNext()) {
				writer.write(bulkJob.get(iterator.next()));
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
