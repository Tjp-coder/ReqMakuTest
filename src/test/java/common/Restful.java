package common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;


/**
 * 用于每一条用例参数的初始化
 * 暂不使用
 * */
@Deprecated
@Data
public class Restful {
    public String baseURI;
    public String basePath;
    public Integer port;
    public String method;
    public Map<String,?> header = new HashMap<>();
    public Map<String,?> query = new HashMap<>();
    public Map<String,?> pathParams = new HashMap<>();
    public Map<String,?> body;
    public String bodyRaw;
    public String contentType;
}