package com.rc.temp.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> void setCacheObject(final String key,final T value){
        redisTemplate.opsForValue().set(key,value);
    }

    public <T> void setCacheObject(final String key, final T value, final Integer timeOut, final TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value,timeOut,timeUnit);
    }

    public boolean expire(final String key,final Long timeout){
        return expire(key,timeout,TimeUnit.SECONDS);
    }
    public boolean expire(final String key,final Long timeout,final TimeUnit unit){
        return redisTemplate.expire(key,timeout,unit);
    }
    public <T> T getCacheObject(final String key){
        ValueOperations<String,T> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }
    public boolean deleteObject(String key){
        return redisTemplate.delete(key);
    }
    //删除集合
    public Long deleteObject(final Collection collection){
        return redisTemplate.delete(collection);
    }

    public <T> List<T> setCacheList(String key,T item){
        ListOperations list = redisTemplate.opsForList();
        list.leftPush(key,item);
        List<T> all = list.range(key, 0, -1);
        return all;
    }
    public <T> List<T> getCacheList(String key){
        return redisTemplate.opsForList().range(key,0,-1);
    }
    public <T> void setItem(String key,Integer index,T item){
        redisTemplate.opsForList().set(key,index,item);
    }
    public <T> List<T> remCacheList(String key,T item){
        ListOperations list = redisTemplate.opsForList();
        list.remove(key,1,item);
        List all = list.range(key, 0, -1);
        return all;
    }
    public <T> Long remList(String key,Integer count,T value){
        ListOperations list = redisTemplate.opsForList();
        Long result = list.remove(key, count, value);
        return result;
    }

    public <T> SetOperations setCacheSet(final String key, final Set<T> dataSet){
        SetOperations set = redisTemplate.opsForSet();
        Iterator<T> iterator = dataSet.iterator();
        while (iterator.hasNext()){
            set.add(iterator.next());
        }
        return set;
    }
    public <T> Set<T> getCacheSet(final String key){
        return redisTemplate.opsForSet().members(key);
    }

    public <T> Set<T> getKeys(String suffix){
        return redisTemplate.keys("*".concat(suffix));
    }
}
