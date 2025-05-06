package cn.coblog.retrydemo;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryState;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.policy.CircuitBreakerRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * Description TODO
 *
 * @author collby
 * @version 1.0
 * @date 2025/4/24 11:17
 */
@Service
public class RemoteApiService {

	@Resource
	private RetryTemplate retryTemplate;

	private Logger logger = LoggerFactory.getLogger(getClass());

	// *********** 注解式 ***********
	// 当抛出 Exception 时重试，最多3次，延迟初始2秒，每次间隔乘1.5倍
//	@Retryable(retryFor = Exception.class, maxAttempts = 3,recover = "retryTe",
//			backoff = @Backoff(delay = 2000, multiplier = 1.5))
	@Retryable(retryFor = Exception.class,recover = "retryTe",
			backoff = @Backoff(delay = 1000))
	@CircuitBreaker(
			maxAttempts = 1,
			openTimeout = 5000,
			resetTimeout = 10000,
			recover = "retryTe2"
	)
	public boolean pay(int num) throws Exception {
		logger.info("调用第三方接口...");
		if (num == 0) {
			throw new Exception("模拟异常，触发重试");
		}
		return true;
	}

	// 重试失败后执行恢复逻辑
	@Recover
	public boolean retryTe(Exception e, int num) {
		logger.info("重试失败，执行恢复方法，参数num={}", num);
		return false;
	}

	@Recover
	public boolean retryTe2(int num) {
		logger.info("熔断执行"+ num);
		return false;
	}



	// *********** 编程式 ***********
	@Async("retryExecutor")
	public void asyncPayment(String orderId) {
		retryTemplate.execute(context -> {
			callThirdPartyPayment(orderId); // 调用支付接口
			return null;
		});
	}

	private void callThirdPartyPayment(String orderId) {
		if (Math.random() < 0.5) {
			throw new RemoteAccessException("支付接口调用失败");
		}
		System.out.println("支付成功");
	}

	/**
	 * 调用支付接口（模拟失败逻辑）
	 * @param code 0=模拟失败，1=成功
	 */
	public String processPayment(int code) {
		Object key = "circuit";
		boolean isForceRefresh = false;
		RetryState state = new DefaultRetryState(key, isForceRefresh);
		return retryTemplate.execute(
				// RetryCallback（业务逻辑）
				context -> {
					if (code == 0) {
						throw new RuntimeException("支付失败，触发重试");
					}
					return "支付成功";
				},
				// RecoveryCallback（恢复方法）
				context -> {
					System.out.println(code + "所有重试失败，执行恢复逻辑");
					return "恢复结果：支付失败已记录日志";
				},state
		);
	}

	public void test1(){
		RetryTemplate template = new RetryTemplate();
		CircuitBreakerRetryPolicy retryPolicy =
				new CircuitBreakerRetryPolicy(new SimpleRetryPolicy(3));
		retryPolicy.setOpenTimeout(5000);
		retryPolicy.setResetTimeout(20000);
		template.setRetryPolicy(retryPolicy);

		for (int i = 0; i < 10; i++) {
			try {
				Object key = "circuit";
				boolean isForceRefresh = false;
				RetryState state = new DefaultRetryState(key, isForceRefresh);
				String result = template.execute(new RetryCallback<String, RuntimeException>() {
					@Override
					public String doWithRetry(RetryContext context) throws RuntimeException {
						System.out.println("retry count:" + context.getRetryCount());
						throw new RuntimeException("timeout");
					}
				}, new RecoveryCallback<String>() {
					@Override
					public String recover(RetryContext context) throws Exception {
						return "default";
					}
				}, state);
				System.out.println(result);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

}
