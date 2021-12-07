package com.example.sqljpasqlserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.example.sqljpasqlserver")
public class SqlJpaSqlserverApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SqlJpaSqlserverApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(SqlJpaSqlserverApplication.class, args);
	}

}
