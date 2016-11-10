package fj.swsk.cn.eqapp.map.layer;

import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by xul on 2016/6/21.
 */
public class TianDiTuTiledMapServiceLayer extends TiledServiceLayer {
    private TianDiTuLayerType _mapType;
    private TileInfo tiandituTileInfo;
    private String compactTileLoc;

    public TianDiTuTiledMapServiceLayer(String compactTileLoc) {
        this(null, null, true);
        this.compactTileLoc = compactTileLoc;
    }

    public TianDiTuTiledMapServiceLayer(TianDiTuLayerType mapType, String compactTileLoc) {
        this(mapType, null, true);
        this.compactTileLoc = compactTileLoc;
    }

    public TianDiTuTiledMapServiceLayer(TianDiTuLayerType mapType, UserCredentials usercredentials, String compactTileLoc) {
        this(mapType, usercredentials, true);
        this.compactTileLoc = compactTileLoc;
    }

    private TianDiTuTiledMapServiceLayer(TianDiTuLayerType mapType, UserCredentials usercredentials, boolean flag) {
        super("");
        this._mapType = mapType;
        setCredentials(usercredentials);

        if (flag)
            try {
                getServiceExecutor().submit(new Runnable() {
                    public final void run() {
                        a.initLayer();
                    }

                    final TianDiTuTiledMapServiceLayer a;

                    {
                        a = TianDiTuTiledMapServiceLayer.this;
                    }
                });
                return;
            } catch (RejectedExecutionException _ex) {
            }
    }

    public TianDiTuLayerType getMapType() {
        return this._mapType;
    }

    protected void initLayer() {
        this.buildTileInfo();
        this.setFullExtent(new Envelope(90.93146146525402, 18.546482511842392, 126.21035393190226, 38.449495570658648));
//        this.setDefaultSpatialReference(SpatialReference.create(4490));
        this.setDefaultSpatialReference(SpatialReference.create(4326));
        this.setInitialExtent(new Envelope(119.11, 25.97, 119.49, 26.19));
        super.initLayer();
    }

    public void refresh() {
        try {
            getServiceExecutor().submit(new Runnable() {

                public final void run() {
                    if (a.isInitialized())
                        try {
                            a.b();
                            a.clearTiles();
                            return;
                        } catch (Exception exception) {
                            Log.e("ArcGIS", "Re-initialization of the layer failed.", exception);
                        }
                }

                final TianDiTuTiledMapServiceLayer a;

                {
                    a = TianDiTuTiledMapServiceLayer.this;
                    //super();
                }
            });
            return;
        } catch (RejectedExecutionException _ex) {
            return;
        }
    }

    final void b() throws Exception {
    }

    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception {

        byte[] result = null;
        result = getBundle(level - 7, col, row);
        if (result == null) {
            result = getUrlByte(level, col, row);
        }
        return result;
    }

    private byte[] getBundle2(int level, int col, int row) throws Exception {
        byte[] result = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            String mLevel = Integer.toString(level);
            int levelLength = mLevel.length();
            if (levelLength == 1) {
                mLevel = "0" + mLevel;
            }
            mLevel = "L" + mLevel;

            int rowGroup = 128 * (row / 128);
            String mRow = Integer.toHexString(rowGroup);
            int rowLength = mRow.length();
            if (rowLength < 4) {
                for (int i = 0; i < 4 - rowLength; i++) {
                    mRow = "0" + mRow;
                }
            }
            mRow = "R" + mRow;

            int columnGroup = 128 * (col / 128);
            String mColumn = Integer.toHexString(columnGroup);
            int columnLength = mColumn.length();
            if (columnLength < 4) {
                for (int i = 0; i < 4 - columnLength; i++) {
                    mColumn = "0" + mColumn;
                }
            }
            mColumn = "C" + mColumn;

            String bundleFileName = String.format("%s/%s/%s%s", compactTileLoc + "/Layers/alllayers", mLevel, mRow, mColumn) + ".bundle";
            File f = new File(bundleFileName);
            if (f.exists()) {
                //第二步，读取bundle文件，根据前面分析中所推断出的切片的起始位置和切片的长度获取对应的切片并返回
                int index = 128 * (row - rowGroup) + (col - columnGroup);

                RandomAccessFile isBundle = new RandomAccessFile(bundleFileName, "r");
                isBundle.skipBytes(64 + 8 * index);

                //获取位置索引并计算切片位置偏移量
                byte[] indexBytes = new byte[4];
                isBundle.read(indexBytes, 0, 4);
                long offset = (long) (indexBytes[0] & 0xff)
                        + (long) (indexBytes[1] & 0xff) * 256
                        + (long) (indexBytes[2] & 0xff) * 65536
                        + (long) (indexBytes[3] & 0xff) * 16777216;

                //获取切片长度索引并计算切片长度
                long startOffset = offset - 4;
                isBundle.seek(startOffset);
                byte[] lengthBytes = new byte[4];
                isBundle.read(lengthBytes, 0, 4);
                int length = (int) (lengthBytes[0] & 0xff)
                        + (int) (lengthBytes[1] & 0xff) * 256
                        + (int) (lengthBytes[2] & 0xff) * 65536
                        + (int) (lengthBytes[3] & 0xff) * 16777216;

                byte[] tileBytes = new byte[length];
                int bytesRead = 0;
                if (length > 0) {
                    bytesRead = isBundle.read(tileBytes, 0, tileBytes.length);
                    if (bytesRead > 0) {
                        bos.write(tileBytes, 0, bytesRead);
                    }
                    result = bos.toByteArray();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    private byte[] getUrlByte(int level, int col, int row) throws Exception {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            URL sjwurl = new URL(this.getTianDiMapUrl(level, col, row));
            byte[] buf = new byte[1024];

            HttpURLConnection httpUrl = (HttpURLConnection) sjwurl.openConnection();
            httpUrl.connect();
            BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());

            while (true) {
                int bytes_read = bis.read(buf);
                if (bytes_read > 0) {
                    bos.write(buf, 0, bytes_read);
                } else {
                    break;
                }
            }
            bis.close();
            httpUrl.disconnect();

            return bos.toByteArray();
        } catch (Exception ex) {
            return null;
        }
    }

    private byte[] getBundle(int level, int col, int row) throws Exception {
        int size = 128;
        byte[] result = null;
        FileInputStream isBundle = null;
        FileInputStream isBundlx = null;
        try {
            String bundlesDir = compactTileLoc + "/Layers/alllayers";
            String l = "0" + level;
            int lLength = l.length();
            if (lLength > 2) {
                l = l.substring(lLength - 2);
            }
            l = "L" + l;

            int rGroup = size * (row / size);
            String r = "000" + Integer.toHexString(rGroup);
            int rLength = r.length();
            if (rLength > 4) {
                r = r.substring(rLength - 4);
            }
            r = "R" + r;

            int cGroup = size * (col / size);
            String c = "000" + Integer.toHexString(cGroup);
            int cLength = c.length();
            if (cLength > 4) {
                c = c.substring(cLength - 4);
            }
            c = "C" + c;

            String bundleBase = bundlesDir + "/" + l + "/" + r + c;
            String bundlxFileName = bundleBase + ".bundlx";
            String bundleFileName = bundleBase + ".bundle";

            int index = size * (col - cGroup) + (row - rGroup);
            //行列号是整个范围内的，在某个文件中需要先减去前面文件所占有的行列号（都是128的整数）这样就得到在文件中的真是行列号
            isBundlx = new FileInputStream(bundlxFileName);
            isBundlx.skip(16 + 5 * index);
            byte[] buffer = new byte[5];
            isBundlx.read(buffer, 0, 5);
            long offset = (long) (buffer[0] & 0xff) + (long) (buffer[1] & 0xff)
                    * 256 + (long) (buffer[2] & 0xff) * 65536
                    + (long) (buffer[3] & 0xff) * 16777216
                    + (long) (buffer[4] & 0xff) * 4294967296L;

            isBundle = new FileInputStream(bundleFileName);
            isBundle.skip(offset);
            byte[] lengthBytes = new byte[4];
            isBundle.read(lengthBytes, 0, 4);
            int length = (int) (lengthBytes[0] & 0xff)
                    + (int) (lengthBytes[1] & 0xff) * 256
                    + (int) (lengthBytes[2] & 0xff) * 65536
                    + (int) (lengthBytes[3] & 0xff) * 16777216;
            result = new byte[length];
            isBundle.read(result, 0, length);
        } catch (Exception ex) {
            return null;
        } finally {
            if (isBundle != null) {
                isBundle.close();
                isBundlx.close();
            }
        }
        return result;
    }

    @Override
    public TileInfo getTileInfo() {
        return this.tiandituTileInfo;
    }

    private String getTianDiMapUrl(int level, int col, int row) {
        String url = new TDTUrl(level, col, row, this._mapType).generatUrl();
        return url;
    }

    private void buildTileInfo() {
        Point originalPoint = new Point(-180, 90);

        double[] res = {
                1.40625,
                0.703125,
                0.3515625,
                0.17578125,
                0.087890625,
                0.0439453125,
                0.02197265625,
                0.010986328125,
                0.0054931640625,
                0.00274658203125,
                0.001373291015625,
                0.0006866455078125,
                0.00034332275390625,
                0.000171661376953125,
                8.58306884765625e-005,
                4.291534423828125e-005,
                2.1457672119140625e-005,
                1.0728836059570313e-005,
                5.36441802978515E-06//,
                //2.68220901489258E-06,
                //1.34110450744629E-06
        };
        double[] scale = {
                400000000,
                295497598.5708346,
                147748799.285417,
                73874399.6427087,
                36937199.8213544,
                18468599.9106772,
                9234299.95533859,
                4617149.8915429693,
                2308574.9457714846,
                1154287.4728857423,
                577143.73644287116,
                288571.86822143558,
                144285.93411071779,
                72142.967055358895,
                36071.483527679447,
                18035.741763839724,
                9017.8708819198619,
                4508.9354409599309,
                2254.467762533835//,
                //1127.2338812669175,
                //563.616940
        };
        int levels = 19;
        int dpi = 96;
        int tileWidth = 256;
        int tileHeight = 256;
        this.tiandituTileInfo = new com.esri.android.map.TiledServiceLayer.TileInfo(originalPoint, scale, res, levels, dpi, tileWidth, tileHeight);
        this.setTileInfo(this.tiandituTileInfo);
    }
}
