package com.uusafe.platform.test.common.utils;

import redis.clients.jedis.*;


import java.util.*;

public class Jedis extends ShardedJedis{
    private ShardedJedis jedis;
    private LocalJedisPool jedisPool;
    public Jedis(ShardedJedis shardedJedis,LocalJedisPool jedisPool){
        super(new ArrayList<JedisShardInfo>(){
            {
                for(JedisShardInfo info: shardedJedis.getAllShardInfo())
                add(info);
            }

        });
        this.jedisPool=jedisPool;
    }

    @Override
    public void close(){
        this.jedisPool.closeConnection(this);
    }


}
