package com.uusafe.platform.test.common.utils;

import com.uusafe.platform.test.common.bean.annotations.InitMethod;
import com.uusafe.platform.test.common.bean.annotations.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.exceptions.JedisExhaustedPoolException;


import java.util.Queue;

import java.util.concurrent.LinkedBlockingQueue;
@Service
public class LocalJedisPool {
    Queue<Jedis> jedisConnectionQueue = new LinkedBlockingQueue<>();
    LocalJedisPool generatorSubJedisPool(int nums)throws JedisExhaustedPoolException{
        return new LocalJedisPool(nums);
    }

    public LocalJedisPool subJedisPool(int num){
        return new LocalJedisPool(num);
    }
    private static LocalJedisPool instance=null;
    @InitMethod
    public  static LocalJedisPool getInstance(){
        if(instance==null){
            instance=new LocalJedisPool();
        }
        return instance;
    }
    private LocalJedisPool() {
        while (true) {
            try {
                ShardedJedis jedis = SharedJedisTestFactory.getInstance().getJedis();
                jedisConnectionQueue.add(new Jedis(jedis,this));
            } catch (JedisExhaustedPoolException e) {
                break;
            }
        }
    }
    private LocalJedisPool(int num){
        int count=0;
        while(count<num){
            try {
                ShardedJedis jedis = SharedJedisTestFactory.getInstance().getJedis();
                jedisConnectionQueue.add(new Jedis(jedis,this));
            } catch (JedisExhaustedPoolException e) {
                throw new JedisExhaustedPoolException("cannot create sub Pool ,because of pool exhausted");
            }
        }
    }
    public void closeConnection(Jedis jedis){
        this.jedisConnectionQueue.add(jedis);
    }

    public Jedis getJedis(){
        return jedisConnectionQueue.peek();
    }



}
