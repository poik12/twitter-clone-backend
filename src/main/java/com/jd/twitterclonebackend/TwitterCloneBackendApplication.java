package com.jd.twitterclonebackend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class TwitterCloneBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterCloneBackendApplication.class, args);
	}


	@Slf4j
	@Component
	static class BeanInfo implements CommandLineRunner {

		@Autowired
		private ApplicationContext applicationContext;

		@Override
		public void run(String... args) throws Exception {
			log.info("-------------------------------------");
			log.info("Beans:");
			int index = 0;
			for (String beanName : applicationContext.getBeanDefinitionNames()) {
				index += 1;
				log.info("{} ---> {}", index, beanName);
			}
			log.info("-------------------------------------");
		}
	}
}
