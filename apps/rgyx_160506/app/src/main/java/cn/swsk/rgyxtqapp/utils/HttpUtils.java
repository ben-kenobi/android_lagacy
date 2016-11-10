package cn.swsk.rgyxtqapp.utils;

/**
 * Created by tom on 2015/10/17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpUtils {

    public HttpUtils() {
    }


    public static String getJsonContent(String path) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int code = connection.getResponseCode();
            CommonUtils.log("code000==" + code + "");
            if (code == 200) {
                return changeInputStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    public static String postJsonContent(String path, String data) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();
            CommonUtils.log("code==" + url + "");

            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            os.close();
            int code = connection.getResponseCode();
            CommonUtils.log("code==" + code + "");
            if (code == 200) {
                return changeInputStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    public static byte[] dataWithFile(File file) throws Exception {
        FileInputStream is = new FileInputStream(file);
        int len = is.available();
        byte bytes[] = new byte[len];
        is.read(bytes);
        is.close();
        return bytes;
    }

    public static String upload(String path, Map<String, String> map, Map<String, File> files) {
        HttpURLConnection httpURLConnection = null;
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(path);

            httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            CommonUtils.log("code==" + url + "");
            for (String key : map.keySet()) {
                dos.writeBytes(end + twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + end + end);
                dos.write(map.get(key).getBytes());
            }
            int i = 0;
            for (String key : files.keySet()) {
                dos.writeBytes(end + twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"file" + i + "\"; filename=\""
                        + key
                        + "\"" + end);
                dos.writeBytes("Content-Type:application/octet-stream" + end + end);
                dos.write(dataWithFile(files.get(key)));
                i++;
            }
            dos.writeBytes(end + twoHyphens + boundary + twoHyphens);
            dos.flush();


            int code = httpURLConnection.getResponseCode();
            CommonUtils.log("code==" + code + "");
            if (code == 200) {
                return changeInputStream(httpURLConnection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return null;
    }

    /**
     * 将一个输入流转换成指定编码的字符串
     *
     * @param inputStream
     * @return
     */
    private static String changeInputStream(InputStream inputStream) {
        String jsonString = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] data = new byte[1024];
        try {
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
            jsonString = new String(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonString;
    }


    //------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------

    public static void commonRequest(final String path, final Context con, final RequestCB cb) {
        final ProgressDialog pd = DialogUtils.createProgressDialogNshow("", "", true, con);
        CommonUtils.log(path);
        CommonUtils.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {

                final Map<String, Object> map = JsonTools.getMap2(HttpUtils.getJsonContent(path));
                CommonUtils.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (map == null) {
                                CommonUtils.toast("获取数据失败", con);
                                return;
                            }
                            if (cb == null) return;
                            String status = map.containsKey("status") ? map.get("status")
                                    .toString() : "";

                            if ("0".equals(status)) {
                                cb.cb(map, null, 0);
                            } else {
                                if("-1".equals(status)&&map.containsKey("msg")){
                                    CommonUtils.toast(map.get("msg").toString(),con);

                                }else {
                                    CommonUtils.toast(map.containsKey("errmsg") ? map.get("errmsg")
                                            .toString() : "未知错误", con);
                                }
                            }
                        } finally {
                            pd.dismiss();

                        }

                    }
                });


            }
        });
    }


    public static void commonRequest2(final String path, final Context con, final RequestCB cb) {
        final ProgressDialog pd = DialogUtils.createProgressDialogNshow("", "", true, con);
        CommonUtils.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {

                final String resp = HttpUtils.getJsonContent(path);
                CommonUtils.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (cb == null) return;
                            if (resp == null) {
                                CommonUtils.toast("获取数据失败", con);
                                return;
                            }

                            cb.cb(null, resp, 0);
                        } finally {
                            pd.dismiss();
                        }

                    }
                });


            }
        });
    }




    public static void commonRequest3(final String path, final Context con, final String content,
                                      final RequestCB cb) {
        final ProgressDialog pd = DialogUtils.createProgressDialogNshow("", "", true, con);
        CommonUtils.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {

                final Map<String, Object> map = JsonTools.getMap2(HttpUtils.postJsonContent(path,
                        content));
                CommonUtils.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            if (map == null) {
                                CommonUtils.toast("获取数据失败", con);
                                return;
                            }
                            if (cb == null) return;
                            String status = map.containsKey("status") ? map.get("status")
                                    .toString() : "";

                            if ("0".equals(status)) {
                                cb.cb(map, null, 0);
                            } else {

                                    CommonUtils.toast(map.containsKey("errmsg") ? map.get("errmsg")
                                            .toString() : "未知错误", con);
                                cb.cb(map, null, 1);
                            }
                        } finally {
                            pd.dismiss();
                        }

                    }
                });


            }
        });
    }


    public static void commonRequest4(final String path, final Context con, final Map<String,
            String> mapo, final Map<String, File> files,
                                      final RequestCB cb) {
        final ProgressDialog pd = DialogUtils.createProgressDialogNshow("", "", true, con);
        CommonUtils.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {

                final Map<String, Object> map = JsonTools.getMap2(HttpUtils.upload(path,
                        mapo, files));
                CommonUtils.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtils.log("request4||||"+map.toString());
                        try {
                            if (map == null) {
                                CommonUtils.toast("获取数据失败", con);
                                return;
                            }
                            if (cb == null) return;
                            String status = map.containsKey("status") ? map.get("status")
                                    .toString() : "";

                            if ("0".equals(status)) {
                                cb.cb(map, null, 0);
                            } else {
                                CommonUtils.toast(map.containsKey("errmsg") ? map.get("errmsg")
                                        .toString() : "未知错误", con);
                                cb.cb(map, null, 1);
                            }
                        } finally {
                            pd.dismiss();
                        }

                    }
                });


            }
        });
    }
    public static void commonRequest5(final String path, final Context con, final Map<String,
            String> mapo, final Map<String, File> files,
                                      final RequestCB cb) {

        final ProgressDialog pd = DialogUtils.createProgressDialogNshow("提示", "正在提交中，请稍等...",
                true, con);

        com.lidroid.xutils.HttpUtils httpUtils = new com.lidroid.xutils.HttpUtils();

        RequestParams params = new RequestParams();
        for (String key : mapo.keySet()) {
            params.addBodyParameter(key, mapo.get(key));
        }


        for (String key : files.keySet()) {
            params.addBodyParameter(key, files.get(key));
        }

        try {
            httpUtils.send(HttpRequest.HttpMethod.POST, path, params, new RequestCallBack<String>
                    () {
                @Override
                public void onFailure(HttpException e, String msg) {
                    Toast.makeText(con, "提交失败", Toast.LENGTH_SHORT).show();
                    if (cb != null)
                        cb.cb(null, msg, 2);
                    pd.dismiss();
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    pd.dismiss();

                    Map<String, Object> map = JsonTools.getMap(responseInfo.result);
                    CommonUtils.log("request5======"+map.toString());
                    if (map == null) {
                        CommonUtils.toast("获取数据失败", con);
                        if (cb != null)
                            cb.cb(map, null, 1);
                        return;
                    }
                    if (cb == null) return;
                    String status = map.containsKey("status") ? map.get("status")
                            .toString() : "";

                    if ("0".equals(status)) {
                        cb.cb(map, null, 0);
                    } else {
                        CommonUtils.toast(map.containsKey("errmsg") ? map.get("errmsg")
                                .toString() : "未知错误", con);
                        cb.cb(map, null, 1);
                    }

                }

            });
        } catch (Exception e) {
            Toast.makeText(con, "提交数据异常", Toast.LENGTH_SHORT).show();
            if (cb != null)
                cb.cb(null, e.getMessage(), 3);
        } finally {

        }
    }


    public static interface RequestCB {
        public void cb(Map<String, Object> map, String resp, int type);
    }
}
