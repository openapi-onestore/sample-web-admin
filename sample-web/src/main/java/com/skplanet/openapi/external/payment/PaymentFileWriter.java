package com.skplanet.openapi.external.payment;

import java.io.File;

public class PaymentFileWriter {
	class FilePayment {
		
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
		file.canRead();
		
		;
	}
	
}
