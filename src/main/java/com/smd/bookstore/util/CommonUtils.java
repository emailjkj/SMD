package com.smd.bookstore.util;

import java.util.regex.Pattern;

public class CommonUtils {

	public static String regex = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";

	public static boolean isValidIsbn(String isbn) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(isbn).matches();

	}
}
