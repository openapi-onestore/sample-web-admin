package com.skplanet.openapi.vo.payment;

public class FilePaymentResult {

	private String resultCode;
	private String resultMsg;
	private int watingJobs;
	private String jobId;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public int getWatingJobs() {
		return watingJobs;
	}
	public void setWatingJobs(int watingJobs) {
		this.watingJobs = watingJobs;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
}
