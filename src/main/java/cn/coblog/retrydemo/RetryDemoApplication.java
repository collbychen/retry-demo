package cn.coblog.retrydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class RetryDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetryDemoApplication.class, args);
	}

}
