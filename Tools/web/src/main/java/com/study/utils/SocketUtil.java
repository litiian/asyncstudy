package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocketUtil {
	
	private static byte result;

	/*
	 * 十六进制字符串 转换成bytes
	 */  
	public static byte[] hexStr2Bytes(String src) {  
	    int m = 0, n = 0;  
	    int l = src.length() / 2;  
	    byte[] ret = new byte[l];  
	    for (int i = 0; i < l; i++) {  
	        m = i * 2 + 1;
	        n = m + 1;
	        ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));  
	    }  
	    return ret;  
	}  
	private static byte uniteBytes(String src0, String src1) {  
	    byte b0 = Byte.decode("0x" + src0).byteValue();  
	    b0 = (byte) (b0 << 4);  
	    byte b1 = Byte.decode("0x" + src1).byteValue();  
	    byte ret = (byte) (b0 | b1);  
	    return ret;  
	}
	
	
	/** 
	 * bytes转换成十六进制字符串 
	 */  
	public static String byte2HexStr(byte[] b) {  
	    String hs = "";  
	    String stmp = "";  
	    
	    for (int n = 0; n < b.length; n++) {  
	        stmp = (Integer.toHexString(b[n] & 0XFF));  
	        if (stmp.length() == 1)  
	            hs = hs + "0" + stmp;  
	        else  
	            hs = hs + stmp;  
	        // if (n<b.length-1) hs=hs+":";  
	    }  
	    return hs.toUpperCase();  
	}  
	
	/**
	 * 获取校验码
	 */
	public static String getBcc(byte[] bytes){
		
		for (int i = 0; i < bytes.length; i++) {
			if (i == 0) {
				 result = bytes[i];
			} else {
				 result = (byte) (result ^ bytes[i]);
			}
		}
		
		String stmp = Integer.toHexString(result & 0xFF);
		String hs = "";
		if (stmp.length() == 1) {
			hs = "0" + stmp;
		}else {
			hs = stmp;
		}
		return hs.toUpperCase(); 
	}
	
	public static byte[] str2Bytes(String str){
		byte[] bs = new byte[str.length()];
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			bs[i] = (byte)c;
		}
		
		return bs;
	}
	
	public static void addByte2List(List<Byte> list, byte[] bytes){
		for (byte b : bytes) {
			list.add(b);
		}
	}
	
	public static void main(String[] args) {
//		byte[] bs = str2Bytes("##");
//		System.out.println(byte2HexStr(bs));
		
//		int a = 1000;
//		String bin = Integer.toBinaryString(a);
//		System.out.println(bin);
//		int cvt = Integer.parseInt(bin, 2);
//		System.out.println(cvt);
//		byte[] bs = int2Byte2(a);
//		String numstr = byte2HexStr(bs);
//		System.out.println(Integer.parseInt(numstr, 16));
		
//		System.out.println(Integer.parseInt("067E391B", 16));
//		System.out.println(lpad(Integer.toHexString(108935451).toUpperCase(), 8));
//		System.out.println(hex2Str("6263653132613733376462663830343861393066"));
		
//		System.out.println(SocketUtil.lpad(SocketUtil.str2Hex("autotest"), 24));
//		System.out.println(lpad("12345", 16));
//		System.out.println(hex2Str("5755584959555446434330303230303935"));
		
//		System.out.println(byte2HexStr(str2Bytes("306432403009015855")));	
//		System.out.println(Integer.parseInt("0201", 16));
		
//		byte[] bytes = SocketUtil.hexStr2Bytes("23FE");
//		System.out.println(Arrays.toString(bytes));
		
//		char c = '#';
//		System.out.println(Integer.toBinaryString(c));
		
//		System.out
//				.println(getBcc(hexStr2Bytes("02FE575558495955544643433030323030393502007A11050B0E1B2701010301004E0002A75615A4271F4C012E0E370000020101014B4FA94E4064161C271F030001000200030001040005010006070008010104020000014A050006D107CE015A110C0601010F30016D0EF6010139010B360700000000000000000008010115A4271F00010001010F2A090101000139")));
		
//		System.out.println(Integer.parseInt("12", 16));
		
//		System.out.println(hex2Str("5755584959555446434330303230303935", false));
//		System.out.println(str2Hex("555656565656555556555656565656565656565656565656"));
		
		System.out.println(Integer.toHexString(18));
		
	}
	
	public static void byte2Str(byte b){
		System.out.println(Integer.toBinaryString(b).substring(32-8));
	}
	
	public static List<Byte> byte2Byte(byte[] bs){
		List<Byte> bytes = new ArrayList<Byte>();
		for (byte b : bs) {
			bytes.add(b);
		}
		return bytes;
	}
	
	public static byte[] int2Byte2(int i){
		byte[] bytes = new byte[2];
		bytes[1] = (byte)(i & 0xFF);
		bytes[0] = (byte)(i >> 8 & 0xFF);
		return bytes;
	}
	
	public static String lpad(String str, int length){
		int times = length - str.length();
		StringBuffer sb = new StringBuffer(str);
		for (int i = 0; i < times; i++) {
			sb.insert(0, "0");
		}
		return sb.toString();
	}
	
	public static byte[] Byte2byte(List<Byte> list){
		byte[] bs = new byte[list.size()];
		for (int i = 0; i < list.size(); i++) {
			bs[i] = list.get(i);
		}
		
		return bs;
	}
	
	public static String hex2Str(String hex){
		return hex2Str(hex, true);
	}
	
	public static String hex2Str(String hex, boolean upper){
		byte[] bs = hexStr2Bytes(hex);
		StringBuffer sb = new StringBuffer();
		for (byte b : bs) {
			if (0 != b) {
				sb.append((char)b);
			}
		}
		if (upper) {
			return sb.toString().toUpperCase();
		}
		return sb.toString();
	}
	public static String str2Hex(String vin) {
		return byte2HexStr(str2Bytes(vin));
	}
}
