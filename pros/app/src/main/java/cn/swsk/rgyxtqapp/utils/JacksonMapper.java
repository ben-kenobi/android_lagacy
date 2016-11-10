package cn.swsk.rgyxtqapp.utils;

/**
 * Created by tom on 2015/10/17.
 */
import org.codehaus.jackson.map.ObjectMapper;

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

    private JacksonMapper() {
    }

    public static ObjectMapper getInstance() {
        return mapper;
    }

}
