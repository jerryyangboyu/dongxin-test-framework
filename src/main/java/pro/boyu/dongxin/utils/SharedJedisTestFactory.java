package com.uusafe.platform.test.common.utils;

import net.sf.json.JSONObject;
import org.I0Itec.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisExhaustedPoolException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SharedJedisTestFactory {
    private static final Logger logger = LoggerFactory.getLogger(SharedJedisTestFactory.class);
    private static SharedJedisTestFactory instance = null;
    private int MAX_ACTIVE = 512;
    private int MAX_IDLE = 256;
    private int MAX_WAIT = 5120;
    private int TIME_OUT = 3000;
    private boolean TEST_ON_BORROW = true;
    private boolean TEST_WHILE_IDLE = true;
//    private static ZkHelp zkHelp = ZkHelp.getInstance();
    private ShardedJedisPool jedisShardedPool = null;

    private SharedJedisTestFactory() {
    }

    public static synchronized SharedJedisTestFactory getInstance(String zkPath) {
        if (instance == null) {
            instance = new SharedJedisTestFactory();
            IZkDataListener listener = new IZkDataListener() {
                public void handleDataDeleted(String dataPath) {
                    SharedJedisTestFactory.logger.info("Redis node data has been deleted : " + dataPath);
                }

                public void handleDataChange(String dataPath, Object data) {
                    SharedJedisTestFactory.logger.info("Redis node data has been changed : " + dataPath);
                    String redisServerInf = data.toString();
                    if (dataPath.startsWith("/dev")) {
                        SharedJedisTestFactory.instance.initialPool(dataPath.substring(4));
                    } else {
                        SharedJedisTestFactory.instance.initialPool(dataPath);
                    }

                    SharedJedisTestFactory.logger.info("Redis cache node [{}] connection pool has been rebuild : {}", redisServerInf, dataPath);
                }
            };
//            zkHelp.subscribeDataChanges(zkPath, listener);
            instance.initialPool(zkPath);
        }

        return instance;
    }

    public static SharedJedisTestFactory getInstance() {
        if (instance == null) {
            instance = new SharedJedisTestFactory();
            instance.initialPool(null);
        }
        return instance;
    }

    public JSONObject getRedisInfo(String path) throws IOException {
        return loadConfigFile();
    }

    private JSONObject loadConfigFile() throws IOException {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("redis.json");
            if (is == null) throw new FileNotFoundException("UNSUCCESSFUL REDIS TEST, CANNOT FOUND REDIS CONFIG FILE");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder configFiles = new StringBuilder();
            for (String line; (line = reader.readLine()) != null;) {
                configFiles.append(line);
            }
            return JSONObject.fromObject(configFiles.toString());
        } catch (IOException ioException) {
            System.out.println("ERROR file load err...");
            System.exit(-1);
        }
        return null;
    }

    public void initialPool(String path) {
        try {
            List<JedisShardInfo> listJedisShardInfo = new ArrayList();
            JSONObject redisInfoObj = loadConfigFile();
            String serverString = redisInfoObj.getString("cluster");
            this.MAX_ACTIVE = redisInfoObj.getInt("maxActive");
            this.MAX_IDLE = redisInfoObj.getInt("minIdle");
            this.MAX_WAIT = redisInfoObj.getInt("maxWait");
            this.TIME_OUT = redisInfoObj.getInt("timeOut");
            logger.info("path={}, serverString={}", path, serverString);
            String[] serverArray = serverString.split("[,]");
            String[] var8 = serverArray;
            int var9 = serverArray.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                String server = var8[var10];
                String[] redisInfo = server.split("[:]");
                JedisShardInfo jsi = new JedisShardInfo(redisInfo[0], Integer.parseInt(redisInfo[1]), this.TIME_OUT);
                boolean enableAuth = false;

                try {
                    enableAuth = redisInfoObj.getBoolean("enableAuth");
                } catch (Exception var16) {
                    logger.warn("Enable auth config property no config");
                }

                if (enableAuth) {
                    String password = "";

                    try {
                        password = redisInfoObj.getString("password");
                    } catch (Exception var15) {
                        logger.error("Password property no config");
                    }

                    jsi.setPassword(password);
                }

                listJedisShardInfo.add(jsi);
            }

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(this.MAX_ACTIVE);
            config.setMaxIdle(this.MAX_IDLE);
            config.setMaxWaitMillis((long)this.MAX_WAIT);
            config.setTestOnBorrow(this.TEST_ON_BORROW);
            config.setTestWhileIdle(this.TEST_WHILE_IDLE);
            this.jedisShardedPool = new ShardedJedisPool(config, listJedisShardInfo);
            logger.info("Init [{}] shared redis pool ok", path);
        } catch (Exception var17) {
            logger.error("Init shared redis pool error, path : {}", path, var17);
        }

    }

    public ShardedJedis getJedis()throws JedisExhaustedPoolException {
        
            if (this.jedisShardedPool != null) {
                ShardedJedis resource = this.jedisShardedPool.getResource();
                return resource;
            } else {
                return null;
            }
        
    }
}

