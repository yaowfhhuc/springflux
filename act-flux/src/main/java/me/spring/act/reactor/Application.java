package me.spring.act.reactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import me.spring.act.reactor.configuration.multiDatasource.DynamicDataSourceRegister;


@SpringBootApplication
@Import(DynamicDataSourceRegister.class)
public class Application {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		
		for(String name:context.getBeanDefinitionNames()){
			System.out.println(name);;
		}
	}
}
