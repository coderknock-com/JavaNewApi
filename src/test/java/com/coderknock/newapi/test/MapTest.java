package com.coderknock.newapi.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author sanchan
 * @date 2020-05-16
 * @since 1.0.0
 */
public class MapTest {
    Map<String, Integer> testMap;

    @BeforeEach
    public void initMap() {
        testMap = new HashMap<>();
        testMap.put("test", 1);
        testMap.put("hello,world", 2);
        testMap.put("123", 3);
        testMap.put("1023", 4);
        testMap.put("nullValue", null);
    }

    @DisplayName("不可操作 Map")
    @Test
    public void immutableMap() {
        // Java 9 提供创建不可变 Map 注意，实际使用时，将该变量声明为 final 可能会更符合你的预期
        testMap = Map.of(
                "test", 1,
                "hello,world", 2,
                "123", 3,
                "1023", 4
        );
        // 测试是会抛出异常
        assertThrows(UnsupportedOperationException.class, () -> testMap.put("a", 123));
    }

    @DisplayName("lambda 方式迭代器，底层还是使用 entrySet 进行迭代")
    @Test
    public void forEach() {
        testMap.forEach((key, value) -> {
            System.out.println(String.format("%s ----> %s", key, value));
        });
    }

    @Test
    public void getOrDefault() {
        assertEquals(testMap.get("test"), 1);
        assertEquals(testMap.get("hello,world"), 2);
        assertEquals(testMap.get("123"), 3);
        assertEquals(testMap.get("1023"), 4);
        assertEquals(testMap.get("nullValue"), null);
        assertEquals(testMap.get("不存在"), null);
        assertFalse(testMap.containsKey("不存在"));
        assertEquals(testMap.getOrDefault("nullValue", 0), null);
        assertEquals(testMap.getOrDefault("不存在", 0), 0);
    }

    @Test
    public void replaceAll() {
        assertEquals(testMap.get("test"), 1);
        assertEquals(testMap.get("hello,world"), 2);
        assertEquals(testMap.get("123"), 3);
        assertEquals(testMap.get("1023"), 4);
        assertEquals(testMap.get("nullValue"), null);
        testMap.replaceAll((key, value) -> {
            Integer keyInt;
            if (Objects.isNull(value)) {
                return -1;
            }
            try {
                keyInt = Integer.valueOf(key);
            } catch (NumberFormatException e) {
                //说明不是数字
                keyInt = key.length();
            }
            return (keyInt + value);
        });
        assertEquals(testMap.get("test"), 1 + "test".length());
        assertEquals(testMap.get("hello,world"), 2 + "hello,world".length());
        assertEquals(testMap.get("123"), 3 + 123);
        assertEquals(testMap.get("1023"), 4 + 1023);
        assertEquals(testMap.get("nullValue"), -1);
    }

    @DisplayName("putIfAbsent 如果 key 不存在或为 null 才 put")
    @Test
    public void putIfAbsent() {
        assertEquals(testMap.get("nullValue"), null);
        testMap.putIfAbsent("nullValue", 999);
        assertEquals(testMap.get("nullValue"), 999);

        testMap.putIfAbsent("我是一个不存在的 key", 999);
        assertEquals(testMap.get("我是一个不存在的 key"), 999);

        //修改已有值，该方法会修改失败
        assertEquals(testMap.get("hello,world"), 2);
        testMap.putIfAbsent("hello,world", 999);
        assertEquals(testMap.get("hello,world"), 2);
    }

    @DisplayName("remove 如果 key value 匹配则删除 key")
    @Test
    public void remove() {
        assertTrue(testMap.containsKey("nullValue"));
        assertTrue(testMap.remove("nullValue", null));
        assertFalse(testMap.containsKey("nullValue"));

        assertTrue(testMap.containsKey("hello,world"));
        //value 不对则无法删除
        assertFalse(testMap.remove("hello,world", null));
        assertTrue(testMap.containsKey("hello,world"));
        assertTrue(testMap.remove("hello,world", 2));
        assertFalse(testMap.containsKey("hello,world"));

        //删除不存在的 key
        assertFalse(testMap.remove("我是一个不存在的key", null));
    }

    @DisplayName("replace 将对应 oldValue 的 key 的 value 替换为 newValue")
    @Test
    public void replaceOldVlaue() {
        assertEquals(testMap.get("nullValue"), null);
        assertTrue(testMap.replace("nullValue", null, 123));
        assertEquals(testMap.get("nullValue"), 123);

        assertEquals(testMap.get("hello,world"), 2);
        //value 不对则无法替换
        assertFalse(testMap.replace("hello,world", null, 111));
        assertEquals(testMap.get("hello,world"), 2);
        assertTrue(testMap.replace("hello,world", 2, 111));
        assertEquals(testMap.get("hello,world"), 111);

        //replace不存在的 key
        assertFalse(testMap.replace("我是一个不存在的key", null, 222));
    }

    @DisplayName("replace 将存在的 key 的 value 替换为 newValue")
    @Test
    public void replace() {
        assertEquals(testMap.get("nullValue"), null);
        assertEquals(null, testMap.replace("nullValue", 123));
        assertEquals(testMap.get("nullValue"), 123);

        assertEquals(testMap.get("hello,world"), 2);
        assertEquals(2, testMap.replace("hello,world", 111));
        assertEquals(testMap.get("hello,world"), 111);

        //replace不存在的 key
        assertEquals(null, testMap.replace("我是一个不存在的key", 222));
        assertFalse(testMap.containsKey("我是一个不存在的key"));
    }
}