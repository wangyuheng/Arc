package ai.care.arc.dgraph.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author junhao.chen
 * @date 2020/6/8
 * 存入DGraph前会将 JSONObject 转换为JSONString存入DGraph
 */
public class JSONObjectSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        String text = ((JSONObject) object).toJSONString();
        serializer.write(text);
    }
}

