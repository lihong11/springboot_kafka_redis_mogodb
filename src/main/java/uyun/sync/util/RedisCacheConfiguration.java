package uyun.sync.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


/**
 * 类名：RedisCacheConfiguration<br>
 * 描述：<br>
 * 创建人：<br>
 * wangyp 创建时间：2017/10/13 09:18<br>
 *
 * @version v1.0
 */
@Configuration
@EnableCaching
public class RedisCacheConfiguration extends CachingConfigurerSupport {
	private static final Logger logger = LoggerFactory
			.getLogger(RedisCacheConfiguration.class);

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.database}")
	private int database;

	@Value("${spring.redis.master}")
	private String master;

	@Value("${spring.redis.maxTotal}")
	private int maxTotal;

	@Value("${spring.redis.timeWait}")
	private int timeWait;

	@Value("${spring.redis.maxIdle}")
	private int maxIdle;

	@Value("${spring.redis.minIdle}")
	private int minIdle;

	private static JedisSentinelPool pool;

	@PostConstruct
	public void init() {
		logger.info("JedisPool开始注入！！！");
		// 建立连接池配置参数
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数
		config.setMaxTotal(maxTotal);
		// 设置最大阻塞时间，记住是毫秒数milliseconds
		config.setMaxWaitMillis(timeWait);
		// 设置空闲连接
		config.setMaxIdle(maxIdle);
		config.setMinIdle(minIdle);
		Set<String> sentinels = new HashSet<>();
		// 创建连接池
		String[] ipaddrs = host.split(",");
		logger.info("redis服务地址为：{}", host);
		for (String ipaddr : ipaddrs) {
			String[] arrays = ipaddr.split(":");
			sentinels.add(new HostAndPort(arrays[0], Integer
					.parseInt(arrays[1])).toString());
		}
		pool = new JedisSentinelPool(master, sentinels, config, timeout,
				password, database);

		// startMonitorTemp();
		logger.info("redis pool start success");
	}

	/**
	 * jedis从3.0版本开始废弃这个方法，使用{@link redis.clients.jedis.Jedis#close()} <br/>
	 * 但是现在最高版本的api为2.9.0
	 */
	public static void returnResource(Jedis jedis) {
		if (jedis != null)
			pool.returnResource(jedis);
	}

	/**
	 * 当获取连接发生异常时，将返回null
	 */
	@Bean
	public static Jedis getJedis() {
		Jedis jedis;
		try {
			jedis = pool.getResource();
			logger.debug("jedis:" + jedis + ",isBroken="
					+ jedis.getClient().isBroken());
			if (jedis != null && jedis.getClient().isBroken()) {
				logger.debug("失败重连...");
				jedis.getClient().close();
				jedis.getClient().connect();
			}
		} catch (Exception e) {
			logger.error("redis连接获取失败。", e);
			return null;
		}

		if (jedis == null)
			return null;

		return jedis;
	}

}