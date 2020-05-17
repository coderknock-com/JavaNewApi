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
}
