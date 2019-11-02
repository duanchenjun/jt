package com.jt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//表示启动时不需要配置数据源
@SpringBootApplication(exclude=DataSourceAutoConfiguration.class)
public class SpringBootRun {
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootRun.class,args);
	}
}
