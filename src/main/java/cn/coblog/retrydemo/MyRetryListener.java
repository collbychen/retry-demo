package cn.coblog.retrydemo;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

/**
 * Description TODO
 *
 * @author collby
 * @version 1.0
 * @date 2025/4/24 15:45
 */
public class MyRetryListener implements RetryListener {
	@Override
	public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
		System.out.println("重试开始");
		return true;
	}

	@Override
	public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
		System.out.println("第" + context.getRetryCount() + "次重试失败");
	}

	@Override
	public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
		System.out.println("重试结束");
	}
}