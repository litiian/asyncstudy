package com.study.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertyUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);
	
	public static Properties readPropertys() {
		
		Properties properties=new Properties();
		try(InputStream inStream = PropertyUtils.class.getClassLoader().getResourceAsStream("jdbc.properties"); ) {			
			properties.load(inStream);			
		} catch (IOException e) {
			LOGGER.error("读取配置文件jdbc.properties失败",e);
		}
		return properties;
	}

}
