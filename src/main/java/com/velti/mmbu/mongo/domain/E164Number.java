package com.velti.mmbu.mongo.domain;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** @author gvlachogiannis */
public class E164Number implements Serializable {

    private static final String THREE_DIGIT_COUNTRY_CODES = "21[1-368]|2[2-46]\\d|25[0-8]|29[0-17-9]|" +
            "3[57]\\d|38[0-25-9]|42[013]|5[09]\\d|67[02-9]|68[0-35-9]|69[0-2]|80[08]|" +
            "85[02356]|87[08]|88[0-368]|96[0-8]|97[0-79]|99[1-68]";
    private static final String NANP_REGEX = "[\\s\\\\(]*([2-9][0-8]\\d)[\\s-\\\\).,]*([2-9]\\d{2})[\\s-.,]*(\\d{4})";
    private static final String E164_REGEX = "(?:(?:\\+?(1)[\\s-.,]*" + NANP_REGEX
            + ")|(?:\\+?(7)([0-9\\s-\\\\(\\\\).,]{6,}))|"
            + "(?:\\+?(2[07]|3[0-469]|4[0-13-9]|5[1-8]|6[0-6]|8[1-246]|9[0-58])([0-9\\s-\\\\(\\\\).,]{5,}))|\\+?(?:("
            + THREE_DIGIT_COUNTRY_CODES + ")([0-9\\s-\\\\(\\\\).,]{4,})))";

    private static final Pattern NANP_PATTERN = Pattern.compile(NANP_REGEX);
    private static final Pattern E164_PATTERN = Pattern.compile(E164_REGEX);

    private String countryCode;
    private String npa;
    private String nxx;
    private String number = null;

    public E164Number(String rawNumber, boolean international) {
        // unnormalized -> e164 conversion formula goes here

        if (!international) {
            number = matchUSNumber(rawNumber);
        }

        if (number == null) {
            number = matchInternationalNumber(rawNumber);
            if (number == null) {
                throw new NumberFormatException("Invalid number '" + rawNumber + "' - cannot be converted to E164");
            }
        }
    }

    // This method attempts to match domestic (US) numbers with no country code (without the leading 1).
    // Can potentially conflict with international numbers that have 10 digits.
    private String matchUSNumber(String rawNumber) {
        Matcher nanpMatcher = NANP_PATTERN.matcher(rawNumber);
        if (nanpMatcher.matches()) {
            countryCode = "1";
            npa = nanpMatcher.group(1);
            nxx = nanpMatcher.group(2);
            return countryCode + npa + nxx + nanpMatcher.group(3);
        }
        return null;
    }

    // This method matches all numbers, including US numbers with the leading 1
    private String matchInternationalNumber(String rawNumber) {
        Matcher matcher = E164_PATTERN.matcher(rawNumber);
        String result = null;
        if (matcher.matches()) {
            if (matcher.group(1) != null) {
                //US numbers (+1)
                result = handleNANPNumber(matcher);
            } else if (matcher.group(5) != null) {
                //Russian numbers (+7)
                result = handleRussianCountry(matcher);
            } else if (matcher.group(7) != null) {
                //2-digit CCs
                result = handle2DigitCountry(matcher);
            } else if (matcher.group(9) != null) {
                //3-digit CCs
                result = handle3DigitCountry(matcher);
            }
            npa = matcher.group(2);
            nxx = matcher.group(3);

            return result;
        }
        return null;
    }

    private String handleNANPNumber(Matcher matcher) {
        //US numbers (+1)
        countryCode = matcher.group(1);
        String significantNumber = matcher.group(2) + matcher.group(3) + matcher.group(4);
        if (significantNumber.length() == 10) {
            return formatNumber(significantNumber);
        }
        return null;
    }

    private String handleRussianCountry(Matcher matcher) {
        //Russian numbers (+7)
        countryCode = matcher.group(5);
        String significantNumber = matcher.group(6).replaceAll("\\s|-|\\(|\\)", "");
        if (significantNumber.length() >= 6 && significantNumber.length() <= 14) {
            return formatNumber(significantNumber);
        }
        return null;
    }

    private String handle2DigitCountry(Matcher matcher) {
        //2-digit CCs
        countryCode = matcher.group(7);
        String significantNumber = matcher.group(8).replaceAll("\\s|-|\\(|\\)", "");
        if (significantNumber.length() >= 5 && significantNumber.length() <= 13) {
            return formatNumber(significantNumber);
        }
        return null;
    }

    private String handle3DigitCountry(Matcher matcher) {
        //3-digit CCs
        countryCode = matcher.group(9);
        String significantNumber = matcher.group(10).replaceAll("\\s|-|\\(|\\)", "");
        if (significantNumber.length() >= 4 && significantNumber.length() <= 12) {
            return formatNumber(significantNumber);
        }
        return null;
    }

    private String formatNumber(String significantNumber) {
        return countryCode + significantNumber;
    }

    public static String getNanpRegex() {
        return NANP_REGEX;
    }

    public static String getE164Regex() {
        return E164_REGEX;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getNPA() {
        return npa;
    }

    public String getNXX() {
        return nxx;
    }

    public String getNumber() {
        return number;
    }

    public String toString() {
        return number;
    }
}
