package com.what.yunbao.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class DownloadUtils {
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int DATA_TIMEOUT = 40000;
    private final static int DATA_BUFFER = 1024*5;

    public interface DownloadListener {
        public void downloading(int progress);
        public void downloaded();
    }

    public static long download(String urlStr, File dest, DownloadListener downloadListener) throws Exception {
        long remoteSize = 0;
        long totalSize = -1;
        int downloadStep = 5;//进度条+5
        int updateCount = 0;

        if(dest.exists() && dest.isFile()) {
            dest.delete();
        }

        HttpGet request = new HttpGet(urlStr);

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, DATA_TIMEOUT);
        HttpClient httpClient = new DefaultHttpClient(params);

        InputStream is = null;
        FileOutputStream os = null;
        try {
            HttpResponse response = httpClient.execute(request);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                is = response.getEntity().getContent();
                remoteSize = response.getEntity().getContentLength();
                System.out.println("==========remotesize  "+ remoteSize);
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if(contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                	System.out.println("==========iszip ");
                    is = new GZIPInputStream(is);
                } 
                System.out.println("==========1");
                os = new FileOutputStream(dest);
                byte buffer[] = new byte[DATA_BUFFER];
                int readSize = 0;
                System.out.println("==========2");
                while((readSize = is.read(buffer)) > 0){
                    os.write(buffer, 0, readSize);
                    os.flush();
                    totalSize += readSize;
                    if(downloadListener!= null){
                    	int progress=(int)(totalSize*100/remoteSize);
                    	if((updateCount+5)<=progress){
	                    	updateCount=progress;
	                    	downloadListener.downloading(updateCount);
                    	}
                    }
                }
                if(totalSize < 0) { 
                	System.out.println("==========a");
                    totalSize = 0;
                }
            }
        } finally {
        	System.out.println("==========d");
            if(os != null) {
                os.close();
            }
            if(is != null) {
                is.close();
            }
        }

        if(totalSize < 0) {
        	System.out.println("==========b");
            throw new Exception("Download file fail: " + urlStr);
        }

        if(downloadListener!= null){
        	System.out.println("==========c");
            downloadListener.downloaded();
        }

        return totalSize;
        
          

    }
}
