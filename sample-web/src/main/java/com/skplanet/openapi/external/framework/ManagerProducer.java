package com.skplanet.openapi.external.framework;

public class ManagerProducer {
	
	public static AbstractManagerFactory getFactory(String logPropertyPath) {
		return new ManagerFactory(logPropertyPath);
	}
	
}
