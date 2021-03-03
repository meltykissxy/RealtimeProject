import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;

public class RedisUtilJava {
    public static JedisPool jedisPool;
    public static Jedis getJedisClient() throws IOException {
        if (jedisPool == null) {
            Properties config = PropertiesUtilJava.load("config.properties");
            String host = config.getProperty("redis.host");
            String port = config.getProperty("redis.port");

            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(10);//最大连接数
            jedisPoolConfig.setMaxIdle(4);//最大空闲
            jedisPoolConfig.setMinIdle(4);//最小空闲
            jedisPoolConfig.setBlockWhenExhausted(true);//忙碌时是否等待
            jedisPoolConfig.setMaxWaitMillis(5000);//忙碌时等待时长 毫秒
            jedisPoolConfig.setTestOnBorrow(true);//每次获得连接的进行测试
            jedisPool = new JedisPool(jedisPoolConfig, host, Integer.parseInt(port));
        }
        return jedisPool.getResource();
    }

    public static void main(String[] args) throws IOException {
        Jedis jedisClient = getJedisClient();
        System.out.println(jedisClient.ping());
        jedisClient.close();
    }
}