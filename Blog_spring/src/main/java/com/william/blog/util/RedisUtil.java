package com.william.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.util.annotation.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public final class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time, TimeUnit t) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, t);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 有序集合添加之前没有的元素
     */
    public boolean zAdd(String key,Object value,double score) {
        return redisTemplate.opsForZSet().add(key, value,score);
    }

    /**
     * 获取集合中元素的排名（从大到小排序）
     * @param key
     * @param value
     * @return
     */
    public long zGetRank(String key,Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 若集合中已有此元素，则此元素score+传入参数
     * 若没有此元素，则创建元素。
     * @param key
     * @param value
     * @param score
     */
    public void zIncreamentScore(String key,Object value,double score) {
        redisTemplate.opsForZSet().incrementScore(key, value, score);
    }


    /**
     * 对集合按照分数从小到大排序（默认）
     * 指定位置区间0，-1指排序所有元素
     * 得到的值带有score
     * @param key
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScore(String key) {
        return redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
    }

    /**
     * 对集合按照分数从大到小排序
     * @param key
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeWithScore(String key){
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);
    }

    /**
     * 获取有序集合的大小
     * @param key
     * @return
     */
    public Long zGetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取key集合里面，value值的分数
     * @param key
     * @param value
     * @return
     */
    public double zGetScoreByValue(String key,Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }


    /**
     * 指定分数区间，从大到小排序
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeByScoreWithScores(String key, double start, double end){
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, start, end);
    }


}
