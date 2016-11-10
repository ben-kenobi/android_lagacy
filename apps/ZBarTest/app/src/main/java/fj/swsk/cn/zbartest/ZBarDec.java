package fj.swsk.cn.zbartest;

/**
 * Created by apple on 16/8/2.
 */
public class ZBarDec {
    static {
        System.loadLibrary("libZBarDecoder");
    }

    public ZBarDec() {
    }

    public native String decodeRaw(byte[] var1, int var2, int var3);

    public native String decodeCrop(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7);
}
