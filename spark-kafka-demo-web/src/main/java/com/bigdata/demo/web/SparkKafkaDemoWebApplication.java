package com.bigdata.demo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * @author nengjun.hu
 * @date 2019-10-21
 * @description 启动类
 */
@ComponentScan(basePackages = {"com.bigdata.demo.web.*"})
@SpringBootApplication
public class SparkKafkaDemoWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkKafkaDemoWebApplication.class, args);
	}

}
