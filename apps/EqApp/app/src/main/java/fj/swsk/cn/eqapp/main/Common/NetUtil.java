package fj.swsk.cn.eqapp.main.Common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.subs.collect.M.TsceneProg;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.DialogUtil;
import fj.swsk.cn.eqapp.util.HttpUtils;


/**
 * Created by apple on 16/3/1.
 */
public class NetUtil {

    public static void noNetWorkToast() {
        CommonUtils.toast("网络未打开，请检查网络。");
    }

    public static void connectFailToast() {
        CommonUtils.toast("无法连接到服务器!");
    }


    public static void commonRequest(Context con, final String path, final RequestCB cb) {
        final Dialog dialog = DialogUtil.progressD(con);
        dialog.show();
        IConstants.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                final Map<String, Object> map = JsonTools.getMap(HttpUtils.getJsonContent(path));
                IConstants.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (map == null) {
                            connectFailToast();
                            return;
                        }
                        if (cb == null) return;
                        String status = map.containsKey("status") ? map.get("status").toString() : "";
                        if ("200".equals(status)) {
                            cb.cb(map, null, 0);
                        } else if ("400".equals(status)) {
                            CommonUtils.toast(map.get("msg").toString());
                        } else {
                            CommonUtils.toast("未知错误");
                        }
                    }
                });
            }
        });

    }

    public static void commonRequestNoProgress(final String path, final RequestCB cb) {
        IConstants.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                final Map<String, Object> map = JsonTools.getMap(HttpUtils.getJsonContent(path));
                IConstants.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        if (map == null) {
                            CommonUtils.log("连接出错");
                            return;
                        }
                        if (cb == null) return;
                        String status = map.containsKey("status") ? map.get("status").toString() : "";
                        if ("200".equals(status)) {
                            cb.cb(map, null, 0);
                        } else if ("400".equals(status)) {
                            CommonUtils.log(map.get("msg").toString());
                        } else {
                            CommonUtils.log("未知错误");
                        }
                    }
                });
            }
        });

    }


    public static void getFileLen(Context con, final String path1, final RequestCB cb) {
        IConstants.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                String path = HttpUtils.fullPath(path1);
//                String path = HttpUtils.fullPath("http://61.154.9.242:5551/rgyxtqapp.apk");
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(path);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(3000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        final long len = connection.getContentLength();
                        IConstants.MAIN_HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("len", len);
                                cb.cb(map, null, 0);

                            }
                        });

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }

            }
        });

    }

    public static void dl2F(Context con, final String path1, final String dest, final RequestCB cb) {
        final boolean cancel[] = new boolean[]{false};
        final Dialog dialog = DialogUtil.getCustDialog(con, "正在下载",
                R.layout.softupdate_progress).setNegativeButton(R.string.soft_update_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                        cancel[0] = true;
                    }
                }).create();
        dialog.show();
        final ProgressBar mProgressBar = (ProgressBar) dialog.getWindow().findViewById(R.id.update_progress);
        IConstants.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                String path = HttpUtils.fullPath(path1);
//                String path = HttpUtils.fullPath("http://61.154.9.242:5551/rgyxtqapp.apk");
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(path);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(3000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        mProgressBar.setMax(connection.getContentLength());
                        mProgressBar.setProgress(0);
                        File f = new File(dest);
                        f.setReadable(true, false);
                        f.setWritable(true, false);
                        InputStream is = connection.getInputStream();
                        OutputStream os = new FileOutputStream(f);
                        try {
                            byte[] buf = new byte[1024 * 8];
                            int count;
                            while (!cancel[0] && (count = is.read(buf)) != -1) {
                                os.write(buf, 0, count);
                                mProgressBar.setProgress(mProgressBar.getProgress() + count);
                            }
                            if (cancel[0]) {
                                f.delete();
                            }
                        } finally {
                            is.close();
                            os.close();
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }

                IConstants.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        cb.cb(null, dest, 0);

                    }
                });

            }
        });

    }

    public static HttpHandler<String> commonRequest5(String path, final Context con, final Map<String,
            String> mapo, final Map<String, File> files,
                                      final RequestCB cb) {

        final ProgressDialog pd = DialogUtil.createProgressDialogNshow("提示", "正在提交中，请稍等...",
                false, con);
        path = HttpUtils.fullPath(path);
        com.lidroid.xutils.HttpUtils httpUtils = new com.lidroid.xutils.HttpUtils();

        RequestParams params = new RequestParams();
        for (String key : mapo.keySet()) {
            params.addBodyParameter(key, mapo.get(key));
        }


        for (String key : files.keySet()) {
            params.addBodyParameter(key, files.get(key));
        }
        HttpHandler<String> handler=null;
        try {
            handler = httpUtils.send(HttpRequest.HttpMethod.POST, path, params, new RequestCallBack<String>
                    () {
                @Override
                public void onFailure(HttpException e, String msg) {
                    CommonUtils.log(msg + "====" + e);
                    Toast.makeText(con, "提交失败", Toast.LENGTH_SHORT).show();
                    if (cb != null)
                        cb.cb(null, msg, 2);
                    pd.dismiss();
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                }

                @Override
                public void onCancelled() {
                    super.onCancelled();
                    CommonUtils.log("canceled"+"--------------------");
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    pd.dismiss();

                    Map<String, Object> map = JsonTools.getMap(responseInfo.result);
                    CommonUtils.log("request5======" + map.toString());
                    if (map == null) {
                        CommonUtils.toast("操作失败");
                        if (cb != null)
                            cb.cb(map, null, 1);
                        return;
                    }
                    if (cb == null) return;
                    String status = map.containsKey("status") ? map.get("status")
                            .toString() : "";

                    if ("200".equals(status)) {
                        cb.cb(map, null, 0);
                    } else {
                        CommonUtils.toast(map.containsKey("msg") ? map.get("msg")
                                .toString() : "未知错误");
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
        return handler;
    }










    public static void commonRequest6(final TsceneProg tp,String path, final Context con, final Map<String,
            String> mapo, final Map<String, File> files,
                                                     final RequestCB cb) {

        path = HttpUtils.fullPath(path);
        com.lidroid.xutils.HttpUtils httpUtils = new com.lidroid.xutils.HttpUtils();

        RequestParams params = new RequestParams();
        for (String key : mapo.keySet()) {
            params.addBodyParameter(key, mapo.get(key));
        }


        for (String key : files.keySet()) {
            params.addBodyParameter(key, files.get(key));
        }
        try {
            tp.uploadhandler = httpUtils.send(HttpRequest.HttpMethod.POST, path, params, new RequestCallBack<String>
                    () {
                @Override
                public void onFailure(HttpException e, String msg) {
                    CommonUtils.log(msg + "====" + e);
                    tp.state=2;
                    tp.describe="提交失败";
                    if (cb != null)
                        cb.cb(null, msg, 2);
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    tp.compressing=false;
                    tp.total=total;
                    tp.progress=current;
                }

                @Override
                public void onCancelled() {
                    super.onCancelled();
                    CommonUtils.log("canceled"+"--------------------");
                    tp.state=2;
                    tp.describe="取消上传";
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                    Map<String, Object> map = JsonTools.getMap(responseInfo.result);
                    CommonUtils.log("request5======" + map.toString());
                    if (map == null) {
                        tp.state=2;
                        tp.describe="操作失败";
                        if (cb != null)
                            cb.cb(map, null, 1);
                        return;
                    }
                    if (cb == null) return;
                    String status = map.containsKey("status") ? map.get("status")
                            .toString() : "";

                    if ("200".equals(status)) {
                        cb.cb(map, null, 0);
                    } else {
                        tp.state=2;
                        tp.describe=map.containsKey("msg") ? map.get("msg")
                                .toString() : "未知错误";
                        cb.cb(map, null, 1);
                    }

                }

            });
        } catch (Exception e) {
            tp.state=2;
            tp.describe="提交数据异常";
            if (cb != null)
                cb.cb(null, e.getMessage(), 3);
        } finally {

        }
    }











    public static abstract class RequestCB {
        public abstract void cb(Map<String, Object> map, String resp, int type);

    }

}
