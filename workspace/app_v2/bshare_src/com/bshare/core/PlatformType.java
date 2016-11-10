package com.bshare.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 全部可支持平台，若要增加或刪除平台请修改该枚举
 */
public enum PlatformType implements Parcelable {
    EMAIL("email"), SINAMINIBLOG("sinaminiblog", true), SOHUMINIBLOG("sohuminiblog", true), QQMB("qqmb", true), KAIXIN("kaixin001", true), RENREN("renren", true), FAV115("115"),
    COM139("139"), MAIL139("139mail"), COM51("51"), TAONAN("51taonan"), DIAN9("9dian"), FAV9("9fav"), ASK("ask"), BAIDUCANG("baiducang"), BAIDUHI("baiduhi"), BAOHE("baohe"),
    BGOOGLE("bgoogle"), BYAHOO("byahoo"), CAIMI("caimi"), CFOL("cfol"), CHINANEWS("chinanews"), CHOUTI("chouti"), CLZG("clzg"), CYOLBBS("cyolbbs"), CYZONE("cyzone"),
    DELICIOUS("delicious"), DIG24("dig24"), DIGG("digg"), DIGLOG("diglog"), DIGU("digu"), DIGOO("diigo"), DOUBAN("douban"), DREAM("dream"),
    EVERNOTE("evernote"), FACEBOOK("facebook"), FANFOU("fanfou"), FEIXIN("feixin"), FRIENDFEED("friendfeed"), FUNP("funp"), FWISP("fwisp"), GANNIU("ganniu"), GMW("gmw"),
    HAOEI("haoei"), HEMIDEMI("hemidemi"), HEXUNMB("hexunmb"), IFENGMB("ifengmb"), ifensi("ifensi"), INSTPAPER("instapaper"), ITIEBA("itieba"), LESHOU("leshou"),
    LINKEDIN("linkedin"), LIVEFAV("livefav"), LIVESPACE("livespace"), LOO365("loo365"), MALA("mala"), MARKZHI("markzhi"), MASAR("masar"), MISTERWONG("mister-wong"),
    MOPTK("moptk"), MPLIFE("mplife"), MSN("msn"), MYSHARE("myshare"), MYSPACE("myspace"), NETEASEMB("neteasemb"), NETVIBES("netvibes"), PEOPLEMB("peoplemb"),
    PHONEFAVS("phonefavs"), PINGFM("pingfm"), PLURK("plurk"), POCO("poco"), POLLADIUM("polladium"), PRINTF("printf"), QING6("qing6"), QQSHUQIAN("qqshuqian"),
    QQXIAQYOU("qqxiaoyou"), QZONE("qzone"), READITLATER("readitlater"), REDDIT("reddit"), RENJIAN("renjian"), SHOUJI("shouji"), RENMAIKU("renmaiku"), SINAVIVI("sinavivi"),
    SOUHUBAI("sohubai"), STUMBLEUPON("stumbleupon"), SZONE("szone"), TAOJIANGHU("taojianghu"), TIANYA("tianya"), TONGXUE("tongxue"), TUITA("tuita"), TUMBLR("tumblr"),
    TWITTER("twitter"), USHI("ushi"), WAAKEE("waakee"), WEALINK("wealink"), WOSHAO("woshao"), XIANGUO("xianguo"), XIAOMEISNS("xiaomeisns"), XYWEIBO("xyweibo"),
    YIJEE("yijee"), YOUDAO("youdao"), ZHUANMING("zhuanming"), ZJOL("zjol"), ZUOSA("zuosa"), BSYNC("bsync"), SMS("sms"), MORE("more"), QING("sinaqing"),
    MAIL189("189mail"), SHARE189("189share"), CHANGSHAMB("changshamb"), CHEZHUMB("chezhumb"), DUANKOU("duankou"), DUITANG("duitang"), EASTDAYMB("eastdaymb"),
    GMWEIBO("gmweibo"), GPLUS("gplus"), GTRANSLATE("gtranslate"), HEFEIMB("hefeimb"), HUABAN("huaban"), IFENGKB("ifengkb"), JIANWEIBO("jianweibo"),
    JIPIN("jipin"), JOINWISH("joinwish"), JSCHINA("jschina"), JXCN("jxcn"), LAODAO("laodao"), LEZHIMARK("lezhimark"), MAIKUNOTE("maikunote"), MEILISHUO("meilishuo"),
    MFEIXIN("mfeixin"), MILIAO("miliao"), MINGDAO("mingdao"), MOGUJIE("mogujie"), MQZONE("mqzone"), MWEIBO("mweibo"), PINTEREST("pinterest"), QILEKE("qileke"),
    QINGBIJI("qingbiji"), QQIM("qqim"), REDMB("redmb"), SOHUKAN("sohukan"), SOUTHMB("southmb"), SZMB("szmb"), TIANJI("tianji"), WANSHA("wansha"),
    WO("wo"), XINHUAMB("xinhuamb"), XINMINMB("xinminmb"), YAOLANMB("yaolanmb"), YIDONGWEIBO("yidongweibo"), YOUDAONOTE("youdaonote"), WECHAT("wechat"), WECHAT_PENGYOU("wechatpengyou"), UNKNOWN("unknown");

    private final String platformId;
    private final String platformName;
    private final boolean isOauth;
    //平台显示名前缀
    private static final String NAME_PREFIX = "bshare_pn_";

    private PlatformType(String platformId) {
        this(platformId, NAME_PREFIX + platformId, false);
    }

    private PlatformType(String platformId, boolean isOauth) {
        this(platformId, NAME_PREFIX + platformId, isOauth);
    }


    private PlatformType(String platformId, String platformName) {
        this(platformId, platformName, false);
    }

    private PlatformType(String platformId, String platformName, boolean isOauth) {
        this.platformId = platformId;
        this.platformName = platformName;
        this.isOauth = isOauth;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getPlatfromName() {
        return platformName;
    }

    public boolean isOauth() {
        return isOauth;
    }

    public static PlatformType getPlatfromById(String platformId) {
        if (TextUtils.isEmpty(platformId)) {
            return null;
        }
        PlatformType[] ps = PlatformType.values();
        for (PlatformType p : ps) {
            if (p.getPlatformId().equals(platformId)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.ordinal());
    }

    public static final Creator<PlatformType> CREATOR = new Creator<PlatformType>() {
        @Override
        public PlatformType createFromParcel(final Parcel source) {
            return PlatformType.values()[source.readInt()];
        }

        @Override
        public PlatformType[] newArray(final int size) {
            return new PlatformType[size];
        }
    };

}
