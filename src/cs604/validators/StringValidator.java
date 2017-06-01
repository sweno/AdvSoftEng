package cs604.validators;

public class StringValidator {
	public static boolean validate(String value) {
		// test to see if string isn't empty
        return !(value == null || value.isEmpty());
    }

}
