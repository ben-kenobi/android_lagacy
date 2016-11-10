package fj.swsk.cn.eqapp.conf;


import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public final class PropertiesConfig {
	private static Properties properties;
	static{
		properties=new Properties();
		try {
//			properties.load(PropertiesConfig.class.getClassLoader().getResourceAsStream("app.properties"));
			properties.load(PropertiesConfig.class.getResourceAsStream("/assets/conf.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getProperty(String key){
		return properties.getProperty(key);
	}

	public static String getDefServerIp(){
		return getProperty("defServerIP");
	}











	public static URL getResource(String resourceName) {
		if (resourceName.startsWith("/")) {
			resourceName = resourceName.substring(1);
		} else {
			String pkg = PropertiesConfig.class.getName();
			int dot = pkg.lastIndexOf('.');
			if (dot != -1) {
				pkg = pkg.substring(0, dot).replace('.', '/');
			} else {
				pkg = "";
			}

			resourceName = pkg + "/" + resourceName;
		}

		// Delegate to proper class loader
		ClassLoader loader = PropertiesConfig.class.getClassLoader();
		if (loader != null) {
			Log.e("itag","++++++++++++++++"+resourceName);
			return loader.getResource(resourceName);
		} else {
			return ClassLoader.getSystemResource(resourceName);
		}
	}

}
