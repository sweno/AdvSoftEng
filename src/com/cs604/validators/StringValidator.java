package com.cs604.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {
	public static boolean validate(String value) {
		// test to see if string isn't empty
        return !(value == null || value.isEmpty());
    }
	
	private static final Pattern VALID_Int_REGEX = Pattern.compile("^[0-9]+$");
	private static final Pattern VALID_Double_REGEX = Pattern.compile("^[0-9]+(\\.[0-9]+)?$");
	private static final Pattern VALID_Date_REGEX = Pattern.compile("^20[12][0-9]-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$"); 
	// this will be valid for the next 12 years. if something better hasn't been implemented by then there are larger problems with this code

	public static boolean validInt(String value){
		Matcher matcher = VALID_Int_REGEX.matcher(value);
        return matcher.find(); 
	}
	
	public static boolean validDouble(String value){
		Matcher matcher = VALID_Double_REGEX.matcher(value);
        return matcher.find(); 
	}
	
	public static boolean validdate(String value){
		Matcher matcher = VALID_Date_REGEX.matcher(value);
        return matcher.find(); 
	}


}
