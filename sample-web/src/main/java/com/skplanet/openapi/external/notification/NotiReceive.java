package com.skplanet.openapi.external.notification;

public class NotiReceive {

	private String notify_version;
	private String event;
	private String status;
	private String reg_no;
	private String job_Id;
	private String update_time;
	private String verify_sign;
	
	public String getNotify_version() {
		return notify_version;
	}
	public void setNotify_version(String notify_version) {
		this.notify_version = notify_version;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReg_no() {
		return reg_no;
	}
	public void setReg_no(String reg_no) {
		this.reg_no = reg_no;
	}
	public String getJob_Id() {
		return job_Id;
	}
	public void setJob_Id(String job_Id) {
		this.job_Id = job_Id;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getVerify_sign() {
		return verify_sign;
	}
	public void setVerify_sign(String verify_sign) {
		this.verify_sign = verify_sign;
	}
	
}
