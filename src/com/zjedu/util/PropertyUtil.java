package com.zjedu.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import com.jfinal.kit.StringKit;

public class PropertyUtil {
	private static Properties properties;

	static {
		loadPropertyFile("config.property");
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static Properties loadPropertyFile(String file) {
		if (StringKit.isBlank(file))
			throw new IllegalArgumentException(
					"Parameter of file can not be blank");
		if (file.contains(".."))
			throw new IllegalArgumentException(
					"Parameter of file can not contains \"..\"");

		InputStream inputStream = null;
		String fullFile = null;
		try {
			fullFile = PropertyUtil.class.getResource(file).toURI().toString();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		try {
			inputStream = PropertyUtil.class.getResourceAsStream(file);
			Properties p = new Properties();
			p.load(inputStream);
			properties = p;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Properties file not found: "
					+ fullFile);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Properties file can not be loading: " + fullFile);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (properties == null)
			throw new RuntimeException("Properties file loading failed: "
					+ fullFile);
		return properties;
	}
}
