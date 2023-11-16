package com.eshore.common.core.redis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.eshore.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * spring redis 工具类
 *
 * @author eshore
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
@Slf4j
public class RedisCache {
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 清空缓存
     *
     * @param pattern pattern
     */
    public void flushCache(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && keys.size() > 0) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            log.error("redis delete error:{}", e);
        }
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }


    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }


    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key
     * @param hKey
     */
    public void delCacheMapValue(final String key, final String hKey) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T, R> List<T> getMultiCacheMapValue(final String key, final Collection<R> hKeys) {
        Collection<String> hStrkyes = new ArrayList<>();
        for (R hKey : hKeys) {
            hStrkyes.add(hKey.toString());
        }
        return redisTemplate.opsForHash().multiGet(key, hStrkyes);
    }


    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    public Boolean hasCacheKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 运行脚本
     *
     * @param script
     * @param clazz
     * @param keys
     * @param args
     * @param <T>
     * @return
     */
    public <T> T execute(String script, Class<T> clazz, List keys, Object... args) {
        return (T) redisTemplate.execute(new DefaultRedisScript<>(script, clazz), keys, args);
    }

    public void increment(String key, int n) {
        redisTemplate.boundValueOps(key).increment(n);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 查询key正则
     * @return
     */
    public Set<String> scan(String pattern) {
        return scan(pattern, 1000);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 查询key正则
     * @param count   单次扫描数量
     * @return
     */
    public Set<String> scan(String pattern, int count) {
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        return (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keys = new HashSet<>();
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(count).build())) {
                while (cursor.hasNext()) {
                    keys.add(String.valueOf(keySerializer.deserialize(cursor.next())));
                }
            }
            return keys;
        });
    }


    /**
     * author:walker
     * time: 2023/5/10
     * description:
     * 从hash中获取，如果没有话从使用selectByXXX方法从db中获取，然后获取对象的某个属性
     */
    public <T, R, V> V hget(String key, T hkey,
                            Function<T, R> selectByXX, Function<R, V> getFromObj) {
        V value = getCacheMapValue(key, hkey.toString());
        if (value == null) {
            R obj = selectByXX.apply(hkey);
            if (obj == null) return null;
            value = getFromObj.apply(obj);
            setCacheMapValue(key, hkey.toString(), value);
        }
        return value;
    }


    /**
     * 从hash中获取数据，如果数据不存在，则从方法中进行查找,查找后进行设置
     * key： redis Key
     * hkey: hash Key
     * getOne：方法  单参数单返回结果，  所以使用于查询数据库返回一个结果的查询
     * dto： 参数
     */
    public <T, R, V> V hget(String key, T hkey,
                            Function<R, V> getOne, R dto) {
        V value = getCacheMapValue(key, hkey.toString());
        if (value == null) {
            value = getOne.apply(dto);
            setCacheMapValue(key, hkey.toString(), value);
        }
        return value;
    }


    /**
     * 无参，单返回结果
     */
    public <T, R, V> V hget(String key, T hkey,
                            Supplier<V> notParamGetOne) {
        V value = getCacheMapValue(key, hkey.toString());
        if (value == null) {
            value = notParamGetOne.get();
            setCacheMapValue(key, hkey.toString(), value);
        }
        return value;
    }


    /**
     * author:walker
     * time: 2023/5/10
     * hash 中先获取数据，再进行加法
     * description: 目前没有加分布式锁，也没有直接用synchronized（可能会导致速度慢的可能，后期可以进行统一的修改）
     * getOneByDto和dto主要是用于获取默认数据的方法
     */
    public <T> Number hgetAndAdd(String key,
                                 String hkey,
                                 Number newValue,
                                 Function<T, Number> getOneByDto,
                                 T dto) {
        Number value = getCacheMapValue(key,
                hkey);
        if (value == null) {
            value = 0;
//            value=getOneByDto.apply(dto);
//            if(value==null){
//                value=0;
//            }
        }

        BigDecimal res = NumberUtil.add(value, newValue);
        setCacheMapValue(key,
                hkey, NumberUtil.add(value, newValue));

        return res;
    }


    public <T> void hgetAndReduce(String key,
                                  String hkey,
                                  Number newValue,
                                  Function<T, Number> getOneByDto,
                                  T dto) {
        Number value = getCacheMapValue(key,
                hkey);

        if (value == null) {
            value = getOneByDto.apply(dto);
            if (value == null) {
                value = 0;
            }
        }
        Number res = NumberUtil.sub(value, newValue);
        if (NumberUtil.compare(res.longValue(), 0L) < 0) res = 0;
        setCacheMapValue(key,
                hkey, res);
    }


    /**
     * author:walker
     * time: 2023/5/16
     * description:  hash批量设置hkey,然后获取统计加起来的数据
     * 不过是基于value为数字的，countByHkey方法的返回结果为：KeyNumberDTO类
     * key:健
     * hkeys：需要查询的hkey
     * splitIndex: 当key是复合的时候，需要进行split，然后获取的索引  例如：deptId:deviceId  然后获取第二个deviceId作为索引 则splitIndex=1
     */
    public <T> Number hsetMap(String key,
                              List<String> hkeys,
                              Function<T, List<KeyNumberDTO>> countByHkey,
                              T dto,
                              String keyName,
                              Integer splitIndex) {

        Number count = 0L;
        List<String> notDataKeys = new ArrayList<>();
        List<Number> values = getMultiCacheMapValue(key, hkeys);
        //将存在的数据加起来
        for (int i = 0; i < values.size(); i++) {
            Number value = values.get(i);
            if (value != null) {
                count = NumberUtil.add(count, value);
            } else {
                notDataKeys.add(hkeys.get(i));
            }
        }
        if (CollUtil.isEmpty(notDataKeys)) return count;

        //获取第几个元素的数据  例如 deptId:deviceId   现在只需要获取deviceId的数据
        Set<String> fieldKeySet = new HashSet<>();
        if (splitIndex != null) {
            for (int i = 0; i < notDataKeys.size(); i++) {
                if (!notDataKeys.get(i).contains(":")) {
                    break;
                }
                String[] split = notDataKeys.get(i).split(":");
                String spilitKey = split[splitIndex];
                if (!fieldKeySet.contains(spilitKey)) {
                    fieldKeySet.add(spilitKey);
                }
            }
        } else {
            fieldKeySet = notDataKeys.stream().collect(Collectors.toSet());
        }

        ObjectUtils.setFieldValueByName(dto, keyName, new ArrayList<>(fieldKeySet));
        List<KeyNumberDTO> rs = countByHkey.apply(dto);


        Map<String, Long> map = new HashMap<>();
        if (CollUtil.isNotEmpty(rs)) {
            for (KeyNumberDTO r : rs) {
                map.put(r.getKey(), r.getNum());
                count = NumberUtil.add(count, r.getNum());
            }
        }


        for (String notDataKey : notDataKeys) {
            map.putIfAbsent(notDataKey, 0L);
        }

        setCacheMap(key, map);

        return count;
    }


    public void deleteAll(Collection<String> keys) {
        redisTemplate.delete(keys);
    }
}
