package com.google.code.zeroxcafe;

import java.math.BigInteger;
import java.util.Locale;

public class MathUtils {
	public static boolean isCompatible(String number, int base) {
		number=number.toLowerCase(Locale.US);
		
		for(int i=0; i<number.length(); i++) {
			char ch=number.charAt(i);
			
			if(ch>='0' && ch<='9') {
				if(ch-'0' >= base) {
					return false;
				}
			} else if(ch>='a' && ch<='z') {
				if(ch-'a'+10 >= base) {
					return false;
				}
			} else {
				return false;
			}
		}
		
		return true;
	}
	
	public static String convert(String number, int baseFrom, int baseTo) {
		return new BigInteger(number, baseFrom).toString(baseTo);
	}
}