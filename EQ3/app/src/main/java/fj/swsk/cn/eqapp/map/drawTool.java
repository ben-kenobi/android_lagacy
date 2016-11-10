package fj.swsk.cn.eqapp.map;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;

/**
 * Created by xul on 2016/6/28.
 */
public class drawTool {
    public static Polygon getCircle(Point center, double radius) {
        Polygon circle = new Polygon();
        Point[] points = getPoints(center, radius);
        circle.startPath(points[0]);
        for (int i = 1; i < points.length; i++)
            circle.lineTo(points[i]);
        return circle;
    }

    public static Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }
}
