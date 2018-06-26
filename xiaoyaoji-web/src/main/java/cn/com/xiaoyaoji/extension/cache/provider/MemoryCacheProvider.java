package cn.com.xiaoyaoji.extension.cache.provider;

import cn.com.xiaoyaoji.core.util.JsonUtils;
import cn.com.xiaoyaoji.integration.cache.CacheProvider;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujingjie
 * @date 2016-07-28
 */
public class MemoryCacheProvider implements CacheProvider {
    private static Map<String, Value> dataMap;

    static {
        dataMap = new ConcurrentHashMap<>();
        clearWithInterval();
    }

    /**
     * 定期检查失效的数据 10分钟一次
     */
    private static void clearWithInterval() {
        int delay = 10;
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                for(Iterator<Map.Entry<String,Value>> it = dataMap.entrySet().iterator(); it.hasNext();){
                    Map.Entry<String, Value> entry = it.next();
                    if(entry.getValue().getExpires().getTime()<System.currentTimeMillis()){
                        it.remove();
                    }
                }
            }
        }, delay, delay, TimeUnit.MINUTES);
    }

    public void put(String token, String key, Object data, int expires) {
        if (token == null) {
            return;
        }
        Value value = dataMap.get(token);
        if (value == null) {
            value = new Value();
            dataMap.put(token, value);
        }
        if (!(data instanceof String)) {
            data = JsonUtils.toString(data);
        }
        value.setExpires(new Date(System.currentTimeMillis() + expires * 1000));
        value.putData(key, data);
    }

    public Object get(String token, String key, int expires) {
        if (token == null) {
            return null;
        }
        Value value = dataMap.get(token);
        if (value == null) {
            return null;
        }
        value.setExpires(new Date(System.currentTimeMillis() + expires * 1000));
        return value.getData(key);
    }

    @Override
    public void remove(String table) {
        dataMap.remove(table);
    }

    @Override
    public void remove(String table, String key) {
        Value value = dataMap.get(table);
        if (value == null)
            return;
        value.remove(key);
    }

    public class Value {
        private Date expires;
        private Map<String, Object> data = new ConcurrentHashMap<>();

        public Date getExpires() {
            return expires;
        }

        public void setExpires(Date expires) {
            this.expires = expires;
        }

        public void putData(String key, Object value) {
            data.put(key, value);
        }

        public void remove(String key) {
            data.remove(key);
        }

        public Object getData(String key) {
            return data.get(key);
        }
    }
}
