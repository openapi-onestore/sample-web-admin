package com.skplanet.openapi.external.framework;

public class ManagerProducer {
<<<<<<< HEAD
		
	public static AbstractManagerFactory getFactory(Environment env, String logPropetyFilePath) {
		return new ManagerFactory(env, logPropetyFilePath);
=======
	
	public enum Environment{
		LIVE,
		SANDBOX
	}
	
	public static AbstractManagerFactory getFactory(String propertyPath) {
		return new ManagerFactory(propertyPath);
>>>>>>> d63a03bb2aa477b2acaef4c54ef576695a4235b5
	}
	
	public static AbstractManagerFactory getFactory(Environment env, String logPropetyFilePath) {
		return null;
	}
}
