package com.icanit.app_v2.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {
	public static net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat hpof = new HanyuPinyinOutputFormat();
	static {
		hpof.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hpof.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		hpof.setVCharType(HanyuPinyinVCharType.WITH_V);
	}

	public static String toPinyin(char c) {
		String pinyin="";
		try {
			if (c < 256) {
				pinyin =c+"";
			} else {
				pinyin = PinyinHelper.toHanyuPinyinStringArray(c, hpof)[0];
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return pinyin;
	}

	public static String toPinyin(String str) {
		StringBuffer sb = new StringBuffer();
		String[] pinyin;
		char[] hanzi = str.toCharArray();
		try {
			for (int i = 0; i < hanzi.length; i++) {
				if (hanzi[i] < 256) {
					sb.append(str.charAt(i));
				} else {
					pinyin = PinyinHelper.toHanyuPinyinStringArray(hanzi[i],
							hpof);
					sb.append(pinyin[0]);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static void main(String args[])
			throws BadHanyuPinyinOutputFormatCombination {
		System.out.println(PinyinUtil.toPinyin("ºº×Ö×ªÆ´Òô123  +-"));
		System.out.println(PinyinHelper.toHanyuPinyinString("ºº×Ö×ªÆ´Òô123  +-",
				hpof, ""));
		System.out.println((char) (257));
	}
}
