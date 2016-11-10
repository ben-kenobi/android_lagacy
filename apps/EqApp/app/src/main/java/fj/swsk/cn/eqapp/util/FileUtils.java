package fj.swsk.cn.eqapp.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    public static boolean isReadableFile(File file) {
        return file != null && !file.isDirectory() && file.canRead();
    }

    public static boolean isSystemFile(File file) {
        return !(file.isFile() || file.isDirectory());
    }

    public static File newFile(String parent, String name) {
        File dir = new File(parent);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, name);
        return file;
    }

    // formatFileSize
    public static String formatFileSize(long length) {
        String result = null;
        int sub_string = 0;
        if (length >= 1073741824) {
            sub_string = String.valueOf((float) length / 1073741824).indexOf(
                    ".");
            result = ((float) length / 1073741824 + "000").substring(0,
                    sub_string + 3) + " G";
        } else if (length >= 1048576) {
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0,
                    sub_string + 3) + " M";
        } else if (length >= 1024) {
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0,
                    sub_string + 3) + " K";
        } else if (length < 1024)
            result = Long.toString(length) + " B";
        return result;
    }
    public static String formatFileSize2(long length){
        String[] unit = {"B","K","M","G","T"};
        int idx = 0;
        double len = length;
        while (len>1000&&idx<unit.length-1){
            len/=1000.0;
            idx+=1;

        }
       return String.format("%.2f"+unit[idx],len);
    }

    // getFileTypeName
    public static String getFileTypeName(File file) {
        return file.isDirectory() ? "文件夹" : file.isFile() ? "普通文件" : "系统文件";
    }

    // getApplicationRootDir
    public static File getApplicationRootDir(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return context.getExternalFilesDir(null).getParentFile();
        } else {
            return context.getFilesDir().getParentFile();
        }
    }

    // getDataDir
    public static File getDataDir() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return new File(Environment.getExternalStorageDirectory().getPath()
                    + File.separator + "Android" + File.separator + "src/data");
        } else {
            return new File(Environment.getDataDirectory().getPath()
                    + File.separator + "src/data");
        }
    }


    // mkDir
    public static int mkDir(File parent, String filename) {
        if (parent == null || !parent.exists() || !parent.isDirectory() ||
                !parent.canWrite() || TextUtils.isEmpty(filename))
            return 0;
        File file = new File(parent.getPath() + File.separator + filename);
        if (file.exists())
            return -1;
        if (file.mkdirs())
            return 1;
        return 0;
    }

    // createNewFile
    public static int createNewFile(File parent, String filename) throws IOException {
        if (parent == null || !parent.exists() || !parent.isDirectory() ||
                !parent.canWrite() || TextUtils.isEmpty(filename))
            return 0;
        File file = new File(parent.getPath() + File.separator + filename);
        if (file.exists())
            return -1;
        if (file.createNewFile())
            return 1;
        return 0;
    }


    // deleteFileRecursively
    public static boolean deleteFileRecursively(File file, boolean retainDir) {
        if (!file.exists())
            return true;
        boolean b = true;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                b &= deleteFileRecursively(files[i], false);
            }
            if (!retainDir)
                b &= file.delete();
        } else {
            return file.delete();
        }
        return b;
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir.toString();
        }
        return null;

    }

    public static boolean isFileExists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        File file = new File(filePath);

        return file.exists();
    }

    public static void CopyAssets(Context context, String assetDir, String dir) throws IOException {
        String[] files;

        files = context.getResources().getAssets().list(assetDir);

        File mWorkingPath = new File(dir);
        // if this directory does not exists, make one.
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {

            }
        }

        for (int i = 0; i < files.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
                String fileName = files[i];
                // we make sure file name not contains '.' to be a folder.
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        CopyAssets(context, fileName, dir + fileName + "/");
                    } else {
                        CopyAssets(context, assetDir + "/" + fileName, dir + fileName + "/");
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    outFile.delete();

                if (0 != assetDir.length())
                    in = context.getAssets().open(assetDir + "/" + fileName);
                else
                    in = context.getAssets().open(fileName);
                out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        }
    }

}
