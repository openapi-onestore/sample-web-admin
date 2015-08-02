package com.skplanet.openapi.vo.transaction;

public class Payer {

	private String mdn;
	private String carrier;
	private String email;
	private String authkey;
	
	public String getMdn() {
		return mdn;
	}
	
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}
	
	public String getCarrier() {
		return carrier;
	}
	
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAuthkey() {
		return authkey;
	}
	
	public void setAuthkey(String authkey) {
		this.authkey = authkey;
	}
	
}
