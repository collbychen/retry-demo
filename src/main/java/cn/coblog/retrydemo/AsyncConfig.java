package cn.coblog.retrydemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Description TODO
 *
 * @author collby
 * @version 1.0
 * @date 2025/4/24 15:46
 */
@Configuration
@EnableAsync
public class AsyncConfig {
	@Bean("retryExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setThreadNamePrefix("Retry-Async-");
		return executor;
	}
}
