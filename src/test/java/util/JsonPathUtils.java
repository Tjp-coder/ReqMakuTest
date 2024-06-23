package util;

import base.BaseTest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;

/**
 * 对JSONPATH相关操作的简化
 * */
public class JsonPathUtils {

  /**
   * 返回jsonpath解析，用于设置指定模板参数
   * **/
    public static DocumentContext jsonPathParseByFile(String path){
        return JsonPath.parse(BaseTest.class.getResourceAsStream(path));
    }

    /**
     * 给对应类型值设置一个对应的空值，提供空串、空列表等
     * */
    public static DocumentContext setNullByType(Class clazz, DocumentContext data, String key){
        String type = clazz.getSimpleName();
        if (type.equals("JSONArray")){
            data.set("$."+key,new ArrayList<>());
        }else if (type.equals("String")){
            data.set("$."+key,"");
        }else if (type.equals("Integer")){
            data.set("$."+key,null);
        }else {
            data.set("$."+key,null);
        }
        return data;
    }
}
