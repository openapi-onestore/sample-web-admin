package com.skplanet.openapi.external.framework;

public class ManagerProducer {
	
	public static AbstractManagerFactory getFactory(String propertyPath) {
		return new ManagerFactory(propertyPath);
	}
	
}
