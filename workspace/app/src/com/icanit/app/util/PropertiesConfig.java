package com.icanit.app.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import com.icanit.app.common.IConstants;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.factory.ServiceFactory;

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
