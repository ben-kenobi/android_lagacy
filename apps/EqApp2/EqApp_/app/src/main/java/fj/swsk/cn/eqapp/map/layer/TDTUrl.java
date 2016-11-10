package fj.swsk.cn.eqapp.map.layer;
import java.util.Random;

/**
 * Created by xul on 2016/6/21.
 */
public class TDTUrl {
    private TianDiTuLayerType _tiandituMapServiceType;
    private int _level;
    private int _col;
    private int _row;
    public TDTUrl(int level, int col, int row,TianDiTuLayerType tiandituMapServiceType){
        this._level=level;
        this._col=col;
        this._row=row;
        this._tiandituMapServiceType=tiandituMapServiceType;
    }
    public String generatUrl(){
        switch(this._tiandituMapServiceType){
            case VEC_C:
            case CVA_C:
            case CIA_C:
            case IMG_C:
                return generateTianDiTuUrl();
            case FJ_VEC_C:
            case FJ_CVA_C:
               return generateFjTianDiTuUrl();
            default:
                return null;
        }
    }
    public String generateTianDiTuUrl(){
        /**
         * 天地图矢量、影像
         * */
        StringBuilder url=new StringBuilder("http://t");
        Random random=new Random();
        int subdomain = (random.nextInt(6) + 1);
        url.append(subdomain);
        switch(this._tiandituMapServiceType){
            case VEC_C:
                 url.append(".tianditu.com/DataServer?T=vec_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            case CVA_C:
                url.append(".tianditu.com/DataServer?T=cva_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            case CIA_C:
                url.append(".tianditu.com/DataServer?T=cia_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            case IMG_C:
                url.append(".tianditu.com/DataServer?T=img_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            default:
                return null;
        }
        return url.toString();
    }
    public String generateFjTianDiTuUrl(){
        /**
         * 天地图福建矢量
         * */
        StringBuilder url = new StringBuilder("http://service.fjmap.net/");
        switch(this._tiandituMapServiceType){
            case FJ_VEC_C:
                url.append("/vec_fj/wmts?LAYER=vec_fj&STYLE=vec_fj");
                break;
            case FJ_CVA_C:
                url.append("/cva_fj/wmts?LAYER=cva_fj&STYLE=cva_fj");
                break;
            default:
                return null;
        }
        url.append("&TILEMATRIXSET=Matrix_0&FORMAT=image/png&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0")
                .append("&TILECOL=").append(this._col).append("&TILEROW=").append(this._row)
                .append("&TILEMATRIX=").append(this._level);
        return url.toString();
    }

}
