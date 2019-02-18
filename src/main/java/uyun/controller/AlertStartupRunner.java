package uyun.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author wangyp
 * @version 创建时间：2017年10月12日 下午9:35:31 类说明
 */
@Component
public class AlertStartupRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory
			.getLogger(AlertStartupRunner.class);

	@Autowired
	private EventConsumer eventConsumer;
	@Override
	public void run(String... arg0) throws Exception {
		logger.info("加载启动项，自动轮巡redis获取alert告警数据......");
		try {
			Thread threadAec = new Thread(eventConsumer);
			threadAec.start();
		}catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("启动获取alert告警数据轮巡异常." + e.getMessage());
		}

	}
}