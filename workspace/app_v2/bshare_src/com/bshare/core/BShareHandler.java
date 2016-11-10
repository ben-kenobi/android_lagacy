package com.bshare.core;

import android.os.Parcelable;

/**
 * @author Chris.xue
 * 分享事件处理接口
 */
public interface BShareHandler extends Parcelable {
    /**
     * 当分享行为开始时触发
     *
     * @param platform
     * @param shareItem
     */
    void onShareStart(PlatformType platform, BSShareItem shareItem);

    /**
     * 在分享完成后触发,除新浪微博等使用API方式分享的平台，使用浏览器跳转分享的平台触发该事件不表示用户确实在平台上完成了分享，仅表示相关调用已完成。
     *
     * @param platform
     * @param sr
     */
    void onShareComplete(PlatformType platform, ShareResult sr);

    /**
     * 在新浪微博等调用用户授权并发生错误时触发
     *
     * @param platform
     */
    void onVerifyError(PlatformType platform);

    /**
     * 在新浪微博等调用用户授权并成功获取到授权Token时触发
     *
     * @param platform
     * @param credential
     */
    void onVerifySuccess(PlatformType platform, TokenInfo credential);

    /**
     * 在BShareEditor这个Activity中，当多个平台分享开始时该事件被触发
     *
     * @param stamp 时间戳，用于区别多个平台分享是否属于同一批。
     */
    void onBulkShareStart(long stamp);

    /**
     * 在BShareEditor这个Activity中，当多个平台分享结束时该事件被触发
     * @param stamp 时间戳，用于区别多个平台分享是否属于同一批
     * @param success 成功分享数
     * @param failure 分享失败数
     * @param needAuth 需授权而失败分享数
     */
    void onBulkShareComplete(long stamp, int success, int failure, int needAuth);

}
