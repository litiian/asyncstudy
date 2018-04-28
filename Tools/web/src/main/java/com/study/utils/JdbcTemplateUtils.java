package com.study.utils;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JdbcTemplateUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplateUtils.class);
	
	DriverManagerDataSource dataSource;
	JdbcTemplate jdbcTemplate;
	
	public JdbcTemplateUtils() {
		//初始化数据库
		Properties properties=PropertyUtils.readPropertys();
		this.dataSource = new DriverManagerDataSource();
		this.dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		this.dataSource.setUrl(properties.getProperty("jdbc.url"));
		this.dataSource.setUsername(properties.getProperty("jdbc.username"));
		this.dataSource.setPassword(properties.getProperty("jdbc.password"));
		//设置数据源
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public DriverManagerDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DriverManagerDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	

}
