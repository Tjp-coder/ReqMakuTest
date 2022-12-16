package util;

import base.BaseTest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class JsonPathUtils {

  /**
   * 返回jsonpath解析，用于设置指定模板参数
   * **/
    public static DocumentContext jsonPathParseByFile(String path){
        return JsonPath.parse(BaseTest.class.getResourceAsStream(path));
    }



}
