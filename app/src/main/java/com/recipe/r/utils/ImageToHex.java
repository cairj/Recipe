package com.recipe.r.utils;

/** 
 * 图片转成十六进制 
 * @author Administrator 
 * 
 */  
public class ImageToHex {  
	/* 
	 *  实现字节数组向十六进制的转换
	 */  
	public static String byte2HexStr(byte[] b) {
		StringBuffer sb = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1) {
				sb.append("0" + stmp);
			} else {
				sb.append(stmp);
			}
		}
		return sb.toString();
	}  
}
