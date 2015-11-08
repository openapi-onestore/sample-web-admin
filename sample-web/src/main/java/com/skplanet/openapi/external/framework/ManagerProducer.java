package com.skplanet.openapi.external.framework;

public class ManagerProducer {
	
	public enum Environment{
		LIVE,
		SANDBOX
	}
	
	public static AbstractManagerFactory getFactory(String propertyPath) {
		return new ManagerFactory(propertyPath);
	}
	
	public static AbstractManagerFactory getFactory(Environment env, String logPropetyFilePath) {
		return null;
	}
}
