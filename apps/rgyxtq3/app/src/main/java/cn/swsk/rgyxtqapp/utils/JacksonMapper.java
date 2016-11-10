package cn.swsk.rgyxtqapp.utils;

/**
 * Created by tom on 2015/10/17.
 */
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.text.SimpleDateFormat;

/**
 *
 * @ClassName: com.example.jsondemo.JacksonMapper
 * @Description:ObjectMapper的单例模式
 * @author zhaokaiqiang
 * @date 2014-11-27 下午4:06:52
 *
 */
public class JacksonMapper {

    private static final ObjectMapper mapper = new ObjectMapper();
    static{
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private JacksonMapper() {
    }

    public static ObjectMapper getInstance() {
        return mapper;
    }

}
