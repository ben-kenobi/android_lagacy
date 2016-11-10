package com.icanit.app_v2.util;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesConfig {
	private static Properties properties;
	static{
		properties=new Properties();
		try {
			properties.load(PropertiesConfig.class.getClassLoader().getResourceAsStream("app.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getProperty(String key){
		return properties.getProperty(key);
	}

}
