package cn.swsk.rgyxtqapp.utils;

/**
 * Created by tom on 2015/10/17.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {

    public HttpUtils() {
    }

    public static String getJsonContent(String path){
        HttpURLConnection connection=null;
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int code = connection.getResponseCode();
            CommonUtils.log("code000=="+code+"");
            if(code == 200){
                return changeInputStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null)
                 connection.disconnect();
        }
        return null;
    }

    public static String postJsonContent(String path,byte[] data){
        HttpURLConnection connection=null;
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-type", "application/json;charset=UTF-8");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();
            CommonUtils.log("code==" + url + "");

            OutputStream os=connection.getOutputStream();
            os.write(data);
            os.close();
            int code = connection.getResponseCode();
            CommonUtils.log("code=="+code+"");
            if(code == 200){
                return changeInputStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null)
                connection.disconnect();
        }
        return null;
    }

    /**
     * 将一个输入流转换成指定编码的字符串
     * @param inputStream
     * @return
     */
    private static String changeInputStream(InputStream inputStream) {
        String jsonString = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] data = new byte[1024];
        try {
            while((len=inputStream.read(data))!=-1){
                outputStream.write(data,0,len);
            }
            jsonString = new String(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonString;
    }
}
