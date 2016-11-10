package fj.swsk.cn.eqapp.util;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StringUtil {
	public static final char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	public static final Set<Character> urlBreaker = new HashSet<Character>();
	static {
		urlBreaker.add('(');
		urlBreaker.add(')');
		urlBreaker.add(',');
		urlBreaker.add('\"');
		urlBreaker.add('\'');
		urlBreaker.add(';');
		urlBreaker.add('>');
		urlBreaker.add('<');
		urlBreaker.add('{');
		urlBreaker.add('}');
		urlBreaker.add('[');
		urlBreaker.add(']');

		// local
		urlBreaker.add('。');
		urlBreaker.add('：');
		urlBreaker.add('（');
		urlBreaker.add('）');
		urlBreaker.add('，');
		urlBreaker.add('”');
		urlBreaker.add('‘');
		urlBreaker.add('；');
		urlBreaker.add('《');
		urlBreaker.add('》');

		// space
		urlBreaker.add('\n');
		urlBreaker.add((char) 32);
		urlBreaker.add('\r');
		urlBreaker.add('\t');
		urlBreaker.add('\\');
		urlBreaker.add('\b');
	}
	
	public static final Map<Character,Byte>  hexCharMap=new HashMap<Character,Byte>();
	
	static{
		for(int i=0;i<16;i++){
			if(i<10)
				hexCharMap.put((char)('0'+i), (byte)i);
			else{
				hexCharMap.put((char)('a'+i-10), (byte)i);
				hexCharMap.put((char)('A'+i-10), (byte)i);
			}
		}
	}
	
	public static final char[] tempChary=new char[2];
	
	
	
	
	public static int calEditDistance(String s1,String s2){
		int length1=s1.length();
		int length2=s2.length();
		int[][] fn=new int[length1+1][length2+1];
		for(int i=0;i<=length1;i++)
			fn[i][0]=i;
		for(int i=0;i<=length2;i++)
			fn[0][i]=i;
		char c1,c2;
		for(int i=0;i<length1;i++){
			c1=s1.charAt(i);
			for(int j=0;j<length2;j++){
				c2=s2.charAt(j);
				if(c1==c2){
					fn[i+1][j+1]=fn[i][j];
				}else{
					int replace=fn[i][j]+1;
					int insert=fn[i+1][j]+1;
					int delete=fn[i][j+1]+1;
					int min=replace>insert?insert:replace;
					min = min>delete?delete:min;
					fn[i+1][j+1]=min;
				}
			}
		}
		return fn[length1][length2];
	}
	
	
	public static String ignoreCaseReplaceAll(String content,String tar,String replacement){
		if (TextUtils.isEmpty(tar)) {
			return content.replaceAll(tar,replacement);
		} else {
			StringBuffer sb = new StringBuffer();
			int length  = content.length();
			char c = tar.charAt(0);
			for (int i = 0; i < length; i++) {
				char ch=content.charAt(i);
				if (StringUtil.equalsIgnoreCase(ch, c)) {
					int j;
					for (j = 1; j < tar.length(); j++) {
						int k = i + j;
						if (k > length - 1
								|| !StringUtil.equalsIgnoreCase(
										content.charAt(k),
										tar.charAt(j)))
							break;
					}
					if (j == tar.length()) {
						sb.append(replacement);
						i = i + j-1;
					}else{
						sb.append(ch);
					}
				}else{
					sb.append(ch);
				}
			}
			return sb.toString();
		}
	}
	
	
	public static int lastIndexOf(String content,int startOffset,String tar,boolean wrapSearch){
		if(TextUtils.isEmpty(content)||TextUtils.isEmpty(tar))
			return -1;
		int length=content.length();
		if(startOffset>=length)
			startOffset=length-1;
		
		char c=tar.charAt(tar.length()-1);
		for(int i=startOffset;i>=0;i--){
			if(content.charAt(i)==c){
				int j;
				for(j=tar.length()-2;j>=0;j--){
					int k=i+j-tar.length()+1;
					if(k<0||!(content.charAt(k)==tar.charAt(j)))
						break;
				}
				if(j==-1)
					return i-tar.length()+1;
			}
		}
		
		if(wrapSearch){
			for(int i=length-1;i>startOffset-tar.length()+1&&i>=0;i--){
				if(content.charAt(i)==c){
					int j;
					for(j=tar.length()-2;j>=0;j--){
						int k=i+j-tar.length()+1;
						if(k<0||!(content.charAt(k)==tar.charAt(j)))
							break;
					}
					if(j==-1)
						return i-tar.length()+1;
				}
			}
		}
		
		return -1;
	}
	
	public static int indexOf(String content,int startOffset,String tar,boolean wrapSearch){
		if(TextUtils.isEmpty(content)||TextUtils.isEmpty(tar))
			return -1;
		int length=content.length();
		if(startOffset>length)
			startOffset=length;
		if(startOffset<0) startOffset=0;
		
		char c=tar.charAt(0);
		for(int i=startOffset;i<length;i++){
			if(content.charAt(i)==c){
				int j;
				for(j=1;j<tar.length();j++){
					int k=i+j;
					if(k>length-1||!(content.charAt(k)==tar.charAt(j)))
						break;
				}
				if(j==tar.length())
					return i;
			}
		}
		
		if(wrapSearch){
			for(int i=0;i<startOffset+tar.length()-1&&i<length;i++){
				if(content.charAt(i)==c){
					int j;
					for(j=1;j<tar.length();j++){
						int k=i+j;
						if(k>length-1||!(content.charAt(k)==tar.charAt(j)))
							break;
					}
					if(j==tar.length())
						return i;
				}
			}
		}
		return -1;
	}
	
	
	
	
	
	public static int ignoreCaseLastIndexOf(String content,int startOffset,String tar,boolean wrapSearch){
		if(TextUtils.isEmpty(content)||TextUtils.isEmpty(tar))
			return -1;
		int length=content.length();
		if(startOffset>=length)
			startOffset=length-1;
	
		char c=tar.charAt(tar.length()-1);
		for(int i=startOffset;i>=0;i--){
			if(equalsIgnoreCase(content.charAt(i), c)){
				int j;
				for(j=tar.length()-2;j>=0;j--){
					int k=i+j-tar.length()+1;
					if(k<0||!equalsIgnoreCase(content.charAt(k), tar.charAt(j)))
						break;
				}
				if(j==-1)
					return i-tar.length()+1;
			}
		}
		
		if(wrapSearch){
			for(int i=length-1;i>startOffset-tar.length()+1&&i>=0;i--){
				if(equalsIgnoreCase(content.charAt(i), c)){
					int j;
					for(j=tar.length()-2;j>=0;j--){
						int k=i+j-tar.length()+1;
						if(k<0||!equalsIgnoreCase(content.charAt(k), tar.charAt(j)))
							break;
					}
					if(j==-1)
						return i-tar.length()+1;
				}
			}
		}
		
		return -1;
	}
	
	public static int ignoreCaseIndexOf(String content,int startOffset,String tar,boolean wrapSearch){
		if(TextUtils.isEmpty(content)||TextUtils.isEmpty(tar))
			return -1;
		int length=content.length();
		if(startOffset>length)
			startOffset=length;
		if(startOffset<0) startOffset=0;
		
		char c=tar.charAt(0);
		for(int i=startOffset;i<length;i++){
			if(equalsIgnoreCase(content.charAt(i), c)){
				int j;
				for(j=1;j<tar.length();j++){
					int k=i+j;
					if(k>length-1||!equalsIgnoreCase(content.charAt(k), tar.charAt(j)))
						break;
				}
				if(j==tar.length())
					return i;
			}
		}
		
		if(wrapSearch){
			for(int i=0;i<startOffset+tar.length()-1&&i<length;i++){
				if(equalsIgnoreCase(content.charAt(i), c)){
					int j;
					for(j=1;j<tar.length();j++){
						int k=i+j;
						if(k>length-1||!equalsIgnoreCase(content.charAt(k), tar.charAt(j)))
							break;
					}
					if(j==tar.length())
						return i;
				}
			}
		}
		return -1;
	}
	
	
	public static int indexOfUrlBreakSymbo(String content,int startOffset){
		return indexOfBreakSymbol(content, startOffset, urlBreaker);
	}
	
	public static int indexOfBreakSymbol(String content,int startOffset,Set<Character> bs){
		if(TextUtils.isEmpty(content))
			return -1;
		int length=content.length();
		if(startOffset>length)
			startOffset=length;
		if(startOffset<0) startOffset=0;
		
		for(int i=startOffset;i<length;i++){
			if(bs.contains(content.charAt(i))){
				return i;
			}
		}
		
		return -1;
		
	}
	
	
	
	
	
	
	
	public static boolean equalsIgnoreCase(char c1,char c2){
		if(c1==c2) return true;
		if(isLetter(c1)&&isLetter(c2)){
			int delta=c1-c2;
			if(delta==32||delta==-32)
				return true;
		}
		return false;
	}
	
	public static boolean isLetter(char c){
		return c>=65&&c<=90||c>=97&&c<=122;
	}
	
	public static boolean hasMessyCode(String content){
		for(int i=0;i<content.length();i++){
			if(content.charAt(i)==65533)
				return true;
		}
		return false;
	}
	
	public static String bytesToHexStr(byte[] bytes,int count ,String space) {
		if(count<0) count=0;
		if(count>bytes.length) count = bytes.length;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(byteToHexStr(bytes[i])+space);
		}
		return sb.toString();
	}
	
	
	
	public static byte[] hexStrToBytes(String hexStr){
		hexStr=trimSpaces(hexStr);
		if(TextUtils.isEmpty(hexStr)) return new byte[0];
		if(!isHexStr(hexStr)) return null;
		int b1,i;
		byte b2;
		int length=hexStr.length()/2;
		byte[] bytes = new byte[hexStr.length()%2==0?length:length+1];
		for(i=1;i<hexStr.length();i+=2){
			b2=hexCharMap.get(hexStr.charAt(i));
			b1=(hexCharMap.get(hexStr.charAt(i-1))<<4);
			bytes[(i-1)/2]=(byte)(b1|b2);
		}
		
		if(i==hexStr.length()){
			//TODO
			bytes[bytes.length-1]=hexCharMap.get(hexStr.charAt(hexStr.length()-1));
		}
		return bytes;
	}
	
	public static boolean isHexStr(String hexStr){
		if(TextUtils.isEmpty(hexStr)) return false;
		for(int i=0;i<hexStr.length();i++){
			if(!hexCharMap.containsKey(hexStr.charAt(i)))
				return false;
		}
		return true;
	}
	
	public static String trimSpaces(String str){
		if(TextUtils.isEmpty(str)) return "";
		return str.replaceAll("\\s", "");
	}

	/**
	 * 
	 * 
	 * @param mByte
	 * @return
	 */
	public static String byteToHexStr(byte mByte) {
		tempChary[0] = HEXCHAR[(mByte >>> 4)&0x0F ];
		tempChary[1] = HEXCHAR[mByte & 0X0F];
		String s = new String(tempChary);
		return s;
	}
	
	
	/**
	 * format
	 * @param d
	 * @param scale
	 * @return
	 */
	
	public static String formatNumbers(double d,int scale) {
		BigDecimal bd=new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
		String str = bd.toPlainString();
		String[] sary = str.split("\\.");
		int delta=0;
		if(d<0)
			delta=1;
		if (sary[0].length() > 3+delta) {
			char[] cary = sary[0].toCharArray();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < cary.length; i++) {
				sb.append(cary[i]);
				if (i-delta>=0&&(cary.length - i - 1) % 3 == 0)
					sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			if(sary.length>1)
			sb.append("."	+ sary[1]);
			return sb.toString();
		} else {
			return str;
		}
	}

	public static String decimalFormat(double d) {
		return new DecimalFormat("###,###,###,###,###.##").format(d);
	}
	
	/**
	 * 
	 * @param decimal
	 * @param scale
	 * @return
	 */
	
	public static boolean isDecimal(String decimal, int scale) {
		if (TextUtils.isEmpty(decimal))
			return false;
		return decimal.matches("^[0-9]*\\.?[0-9]{0," + scale + "}$")
				&& Double.parseDouble(decimal) > 0;
	}
	
	public static boolean isNumber(String number) {
		if (number == null)
			return false;
		return number.matches("^\\d+$");
	}
	public static boolean isPhoneNum(String phone) {
		if (phone == null)
			return false;
		return phone.matches("^[1]([3][0-9]{1}|59|56|58|88|89)[0-9]{8}$");
	}
	
	public static int indexOfAry(Object obj,Object[] objs){
		if(objs==null) return -1;
		for(int i=0;i<objs.length;i++){
			if(objs[i].equals(obj))
				return i;
		}
		return -1;
	}
}
