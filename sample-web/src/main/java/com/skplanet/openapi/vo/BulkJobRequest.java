package com.skplanet.openapi.vo;

public class BulkJobRequest {

	private String mid;
	private String status;
	private String reason;
	private String waitingJob;
	private String jobId;
	private String uploadFile;
	private String uploadDate;
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getWaitingJob() {
		return waitingJob;
	}
	public void setWaitingJob(String waitingJob) {
		this.waitingJob = waitingJob;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

}
