package com.study.nettyclient;

public class StringUtil {
	public static String string2HexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	public static String string2HexStringUser(String s) {
		String str = "";

		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		while(str.length()<24){
			str="00"+str;
		}

		return str;
	}
	public static String string2HexStringPass(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		while(str.length()<40){
			str="00"+str;
		}
		return str;
	}
	public static String byte2String(byte[] b) {
		StringBuffer sbyte = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sbyte.append(hex.toUpperCase());
		}
		return sbyte.toString();
	}

	public static String byte2String(byte b) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex.toUpperCase();
	}

	public static String int2word(int index) {

		String word = Integer.toHexString(index);
		String d = "";
		for (int i = 0; i < 4 - word.length(); i++) {
			d += "0";
		}
		return d + word;

	}

	public static String int2dword(int index) {
		String word = Integer.toHexString(index);
		String d = "";
		for (int i = 0; i < 8 - word.length(); i++) {
			d += "0";
		}
		return d + word;
	}

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

	public static String binaryString2hexString(String bString) {
		if (bString == null || bString.equals("") || bString.length() % 8 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4) {
			iTmp = 0;
			for (int j = 0; j < 4; j++) {
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}

	public static String Abyte(byte[] b) {
		StringBuffer sb = new StringBuffer();
		int code = b[2];

		switch (code) {
		case 1:
			sb.append("杞﹁締鐧诲綍");
			break;
		case 2:
			sb.append("瀹炴椂涓婃姤");
			break;
		case 3:
			sb.append("琛ュ彂鎶ユ枃");
			break;
		case 5:
			sb.append("骞冲彴鐧诲綍");
			break;
		default:
			sb.append("鏈畾涔�");
		}
		int code1 = b[3];
		switch (code1) {
		case 1:
			sb.append("鎴愬姛");
			break;
		case 2:
			sb.append("澶辫触");
			break;
		case 3:
			sb.append("VIN閲嶅");
		}
		int code2 = b[21];
		switch (code2) {
		case 1:
			sb.append("涓嶅姞瀵�");
			break;
		case 2:
			sb.append("RSA鍔犲瘑");
			break;
		case 3:
			sb.append("AES128鍔犲瘑");
			break;
		case 254:
			sb.append("鏃犳晥");
			break;
		default:
			sb.append("棰勭暀");
		}

		int code3 = b[23];
		sb.append("鏁版嵁鍗曞厓闀垮害" + code3);

		int year = b[24];
		int month = b[25];
		int day = b[26];
		int h = b[27];
		int m = b[28];
		int s = b[29];
		sb.append(" " + year + "-" + month + "-" + day + " " + h + ":" + m + ":" + s);
		return sb.toString();
	}

	/**
	 * 鑾峰彇鏍￠獙鐮�
	 */
	private static byte result;

	public static String getBcc(byte[] bytes) {

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
		} else {
			hs = stmp;
		}
		return hs.toUpperCase();
	}

	public static String getUsername(String username) {
		String bm = "";
		if (username.length() < 17) {

			for (int i = 0; i < 17 - username.length(); i++) {
				bm += "00";
			}
		}
		return bm+string2HexString(username);
	}

	// 杞寲鍗佸叚杩涘埗缂栫爜涓哄瓧绗︿覆
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
	public static void main(String[] args) {
		System.out.println(StringUtil.toStringHex("0500068028D6020C9B3106".toLowerCase()));
	}
}
