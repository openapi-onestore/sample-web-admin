package com.skplanet.openapi.external.bulkpay;

import java.io.File;

public class PaymentFileWriter {
	class FilePayment {
		private String appId;
		private String productId;
		// ...
	}
	
	private File file = null;
	
	public PaymentFileWriter(File target) {
		this.file = target;
	}
	
	public int writeTx(FilePayment paymentRow) {
		return -1;
	}
	
	public void close() {
		;
	}
	
}
