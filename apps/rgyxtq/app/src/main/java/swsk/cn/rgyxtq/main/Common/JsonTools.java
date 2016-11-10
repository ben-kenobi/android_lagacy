package swsk.cn.rgyxtq.main.Common;

/**
 * Created by tom on 2015/10/17.
 */

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 完成对JSON数据的解析
 *
 */
public class JsonTools {

    public static final ObjectMapper mapper = new ObjectMapper();

    public JsonTools() {

    }

    public static String getJsonString(Object object) throws Exception {
        return mapper.writeValueAsString(object);
    }
//
//    /**
//     * 获取对象数据
//     *
//     * @param key
//     * @param jsonString
//     * @return
//     */
//    public static WorkPlan getPlan(String jsonString) {
//        WorkPlan workPlan = new WorkPlan();
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            workPlan = objectMapper.readValue(jsonString, WorkPlan.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return workPlan;
//    }
//
//    /**
//     * 获取对象数据
//     *
//     * @param key
//     * @param jsonString
//     * @return
//     */
//    public static Person getPerson(String key, String jsonString) {
//        Person person = new Person();
//        try {
//            /*JSONObject jsonObject = new JSONObject(jsonString);
//            JSONObject personObject = jsonObject.getJSONObject(key);
//            person.setId(personObject.getInt("id"));
//            person.setName(personObject.getString("name"));
//            person.setAddress(personObject.getString("address"));*/
//            ObjectMapper objectMapper = new ObjectMapper();
//            //Person p =new Person(1001,"张三","福州至");
//            //String jsonString1 = getJsonString(p);
//            person = objectMapper.readValue(jsonString, Person.class);
//            //Log.d(TAG, person.toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return person;
//    }
//
//    /**
//     * 获取对象数组数据
//     *
//     * @param key
//     * @param jsonString
//     * @return
//     */
//    public static ArrayList<Person> getPersons(String key, String jsonString) {
//        ArrayList<Person> arrayList = new ArrayList<Person>();
//        try {
//            /*JSONObject jsonObject = new JSONObject(jsonString);
//            //返回json的数组
//            JSONArray jsonArray = jsonObject.getJSONArray(key);
//            for(int i=0;i<jsonArray.length();i++){
//                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//                Person person = new Person();
//                person.setId(jsonObject2.getInt("id"));
//                person.setName(jsonObject2.getString("name"));
//                person.setAddress(jsonObject2.getString("address"));
//                list.add(person);
//            }*/
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            arrayList = objectMapper.readValue(jsonString,
//                    new ArrayList<Person>().getClass());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return arrayList;
//    }

    /**
     * 获取String数组数据
     *
     * @param key
     * @param jsonString
     * @return
     */
    public static List<String> getList(String key, String jsonString) {
        List<String> list = new ArrayList<String>();
        try {
            list = mapper.readValue(jsonString, new ArrayList<String>().getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取对象的Map集合数据
     *
     * @param key
     * @param jsonString
     * @return
     */
    public static List<Map<String, Object>> getListMap(String key, String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = mapper.readValue(jsonString, new ArrayList<Map<String, Object>>().getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取对象的Map数据
     *
     * @param jsonString
     * @return
     */
    public static Map<String, Object> getMap(String jsonString) {
        if(jsonString==null) return null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = mapper.readValue(jsonString, new HashMap<String, Object>().getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
    public static byte[] getJsonBytes(Object obj){
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}