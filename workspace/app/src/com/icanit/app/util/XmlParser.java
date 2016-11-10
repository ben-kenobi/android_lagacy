package com.icanit.app.util;

import java.io.ByteArrayInputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.icanit.app.common.IConstants;
import com.icanit.app.entity.PayResultInfo;

public final  class XmlParser {
	public static PayResultInfo parsePayResultXml(byte[] xmlData) throws Exception{
		PayResultInfo payResultInfo = new PayResultInfo();
		XmlPullParser parser =Xml.newPullParser();
		ByteArrayInputStream bais = new ByteArrayInputStream(xmlData);
		parser.setInput(bais,IConstants.TRANSMISSION_CHARSET);
		bais.close();
		int event = parser.getEventType();
		String tagName,text;
		while(event!=XmlPullParser.END_DOCUMENT){
			switch(event){
			case XmlPullParser.START_DOCUMENT:break;
			case XmlPullParser.START_TAG:
				tagName=parser.getName();
				parser.next();text=parser.getText();
				if("application".equals(tagName)){
					payResultInfo.application=text;
				}else if("merchantId".equals(tagName)){
					payResultInfo.merchantId=text;
				}else if("merchantOrderId".equals(tagName)){
					payResultInfo.merchantOrderId=text;
				}else if("merchantOrderTime".equals(tagName)){
					payResultInfo.merchantOrderTime=text;
				}else if("respCode".equals(tagName)){
					payResultInfo.respCode=text;
				}else if("respDesc".equals(tagName)){
					payResultInfo.respDesc=text;
				}
				break;
			case XmlPullParser.END_TAG:break;
			}
			event=parser.next();
		}
		return payResultInfo;
	}
}
