package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @Description: 参数处理工具类
 * */
public class ParamsUtil {
    /**
     * 读文件转成Map
     * @param filePath 文件路径
     * @return Map<String, Object>
     */
    public static Map<String,Object> fileToMap(String filePath) throws IOException {
        File file = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file,Map.class);
    }

    /**
     * json字符串转成Map
     * @param jsonStr String
     * @return Map<String, Object>
     */
    public static Map<String,Object> strToMap(String jsonStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonStr,Map.class);
    }
}
