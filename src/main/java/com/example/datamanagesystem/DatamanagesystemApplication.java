package com.example.datamanagesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.datamanagesystem.mapper") // 扫描Mapper接口
public class DatamanagesystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatamanagesystemApplication.class, args);
	}

}
