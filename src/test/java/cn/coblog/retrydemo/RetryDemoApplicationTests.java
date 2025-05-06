package cn.coblog.retrydemo;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RetryDemoApplicationTests {

	@Resource
	private RemoteApiService remoteApiService;

	@Test
	void contextLoads() throws Exception {
//		remoteApiService.pay(0);
//		String a = "123";
//		String b = new String("123");
//		StringBuffer sb = new StringBuffer("123");
//		String c = "1" + "23";
//		System.out.println(sb.toString() == c);
//		System.out.println(a.equals(b));
//		System.out.println(a.hashCode());
//		System.out.println(b.hashCode());
//		 第一次调用（失败）
//		try {
//			System.out.println("第1次调用结果: " + remoteApiService.processPayment(0));
//		} catch (Exception e) {
//			System.out.println("第1次异常: " + e.getMessage());
//		}
//
//		// 第二次调用（失败，触发熔断）
//		try {
//			System.out.println("第2次调用结果: " + remoteApiService.processPayment(0));
//		} catch (Exception e) {
//			System.out.println("第2次异常: " + e.getMessage());
//		}
//
//		// 熔断后立即调用（直接失败）
//		try {
//			System.out.println("第3次调用结果: " + remoteApiService.processPayment(1)); // 正常参数
//		} catch (Exception e) {
//			System.out.println("第3次异常: " + e.getMessage());
//		}
//
//		// 等待 6 秒（超过熔断时间）
//		Thread.sleep(6000);
//
//		// 熔断冷却后调用（允许试探）
//		try {
//			System.out.println("第4次调用结果: " + remoteApiService.processPayment(1));
//		} catch (Exception e) {
//			System.out.println("第4次异常: " + e.getMessage());
//		}
		remoteApiService.test1();
	}

}
