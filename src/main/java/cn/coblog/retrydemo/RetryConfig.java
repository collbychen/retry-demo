package cn.coblog.retrydemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.CircuitBreakerRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Description TODO
 *
 * @author collby
 * @version 1.0
 * @date 2025/4/24 15:44
 */
@Configuration
public class RetryConfig {
	@Bean
	public RetryTemplate retryTemplate() {

		// 基础重试策略：最多尝试 3 次
		SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
		simpleRetryPolicy.setMaxAttempts(2);

		// 熔断策略配置：
		// - 当连续失败 2 次时触发熔断（open）
		// - 熔断持续 5 秒（5000ms）
		// - 熔断冷却期后，允许 1 次试探请求（resetTimeout=10000ms）
		CircuitBreakerRetryPolicy circuitPolicy = new CircuitBreakerRetryPolicy(simpleRetryPolicy);
		circuitPolicy.setOpenTimeout(5000);
		circuitPolicy.setResetTimeout(10000);

		// 固定重试间隔 1 秒
		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(1000);

		return RetryTemplate.builder()
				.customPolicy(circuitPolicy)
				.customBackoff(backOffPolicy)
				.retryOn(RemoteAccessException.class)
//				.withListener(new MyRetryListener())
				.build();
	}
}
