package com.ebi.ass.utils;

import java.util.Base64;

/**
 * @author MohamedRamadan
 *
 */
public class EncodingUtil {
	
	public static String encodeBase64(String key) {
		return Base64.getEncoder().encodeToString(key.getBytes());
	}
	
}
