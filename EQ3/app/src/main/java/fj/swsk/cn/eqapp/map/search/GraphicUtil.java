package fj.swsk.cn.eqapp.map.search;

import android.graphics.drawable.Drawable;

import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;

import java.util.Map;

/**
 * Created by xul on 2016/6/28.
 */
public class GraphicUtil {
    /**
     * 创建一个图片marker
     *
     * @param drawable
     * @param attributes
     * @param x
     * @param y
     * @return
     */
    public static Graphic createPictureMarkerSymbol(Drawable drawable,
                                              Map<String, Object> attributes,
                                              double x, double y) {
        MarkerSymbol pic = new PictureMarkerSymbol(drawable);

        Point pt = new Point(x, y);
        if (attributes != null) {
            return new Graphic(pt, pic, attributes);
        } else {
            return new Graphic(pt, pic);
        }
    }

    /**
     * 创建一个图片marker
     * @param symbol
     * @param attributes
     * @param x
     * @param y
     * @return
     */
    public static Graphic createPictureMarkerSymbol(PictureMarkerSymbol symbol,
                                              Map<String, Object> attributes,
                                              double x, double y) {
        MarkerSymbol pic = new PictureMarkerSymbol(symbol);

        Point pt = new Point(x, y);
        if (attributes != null) {
            return new Graphic(pt, pic, attributes);
        } else {
            return new Graphic(pt, pic);
        }
    }
}
