package org.mdcconcepts.androidapp.spa.customitems;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to validate input given by user
 * 
 * @author CodeLord
 * 
 */
public class CustomValidator {

	private static final Pattern PHONE_NUMBER_PATTERN = Pattern
			.compile("\\d{10}");
	private static final Pattern CARD_NUMBER_PATTERN = Pattern
			.compile("\\d{16}");
	private static final Pattern CARD_CCV_NUMBER_PATTERN = Pattern
			.compile("\\d{3}");
	private static final Pattern EMAIL_PATTERN = Pattern
			.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	private static final Pattern DATE_PATTERN = Pattern
			.compile("(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)");

	/**
	 * This method is used for validation of Phone Number
	 * 
	 * @param RequestedPhoneNumber
	 * @return
	 */
	public static boolean isValidPhoneNumber(String RequestedPhoneNumber) {

		Matcher m = PHONE_NUMBER_PATTERN.matcher(RequestedPhoneNumber);

		return m.matches();
	}

	/**
	 * This method is used for validation of Card Number
	 * 
	 * @param RequestedCardNumber
	 * @return
	 */
	public static boolean isValidCardNumber(String RequestedCardNumber) {

		Matcher m = CARD_NUMBER_PATTERN.matcher(RequestedCardNumber);

		return m.matches();
	}

	/**
	 * This method is used for validation of Card CCV Number
	 * 
	 * @param RequestedCardCCVNumber
	 * @return
	 */
	public static boolean isValidCardCCVNumber(String RequestedCardCCVNumber) {

		Matcher m = CARD_CCV_NUMBER_PATTERN.matcher(RequestedCardCCVNumber);

		return m.matches();
	}

	/**
	 * This method is used to validate Email Address
	 * 
	 * @param RequestedEmail
	 * @return
	 */
	public static boolean isValidEmail(String RequestedEmail) {

		Matcher m = EMAIL_PATTERN.matcher(RequestedEmail);

		return m.matches();
	}

	/**
	 * This method is used to validate Date
	 * 
	 * @param RequestedDate
	 * @return
	 */
	public static boolean isValidDate(String RequestedDate) {

		Matcher m = DATE_PATTERN.matcher(RequestedDate);

		return m.matches();
	}
}
