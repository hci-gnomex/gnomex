package hci.gnomex.utility;

public class PasswordUtil {

    public static final String REQUIREMENTS_TEXT =
            "Passwords must be 8-25 characters long, contain no spaces" + "\n" +
            "or slashes, and contain three or more of the following:" + "\n" +
            "lowercase letter, uppercase letter, digit, or symbol.";

    public static final String COMPLEXITY_ERROR_TEXT = "The password you provided does not meet complexity requirements.";

    private PasswordUtil() {
    }

    /**
     * Checks to see if the supplied password meets the following requirements:
     *   Must be between 8 and 25 characters long
     *   Contains no whitespace
     *   Contains no slashes "/" "\"
     *   Contains at least three of the following:
     *     * lower case letter
     *     * upper case letter
     *     * digit
     *     * other character
     *
     * @param password
     * @return true if the password meets the requirements, false otherwise
     */
    public static boolean passwordMeetsRequirements(String password) {
        if (password == null) {
            return false;
        }
        if (password.length() < 8 || password.length() > 25) {
            return false;
        }
        boolean containsLowerCase = false;
        boolean containsUpperCase = false;
        boolean containsDigit = false;
        boolean containsWhitespace = false;
        boolean containsSlash = false;
        boolean containsOther = false;
        for (int i = 0; i < password.length(); i++) {
            char single = password.charAt(i);
            if (Character.isLowerCase(single)) {
                containsLowerCase = true;
            } else if (Character.isUpperCase(single)) {
                containsUpperCase = true;
            } else if (Character.isDigit(single)) {
                containsDigit = true;
            } else if (Character.isWhitespace(single)) {
                containsWhitespace = true;
            } else if (single == '/' || single == '\\') {
                containsSlash = true;
            } else {
                containsOther = true;
            }
        }
        if (containsWhitespace || containsSlash) {
            return false;
        }
        int varietyCount = 0;
        varietyCount += containsLowerCase ? 1 : 0;
        varietyCount += containsUpperCase ? 1 : 0;
        varietyCount += containsDigit ? 1 : 0;
        varietyCount += containsOther ? 1 : 0;
        return (varietyCount >= 3);
    }

    private static void assertPasswordMeetsRequirements(String password, boolean expected) {
        boolean actual = passwordMeetsRequirements(password);
        String quotedPassword = (password == null) ? "null" : "\"" + password + "\"";
        String output = "passwordMeetsRequirements(" + quotedPassword + ") = " + actual;
        if (actual == expected) {
            output += " (expected)";
        } else {
            output += ", expected " + expected + ". NOOOOOOOOOOOOOOOOOOOOOOOOOO!";
        }
        System.out.println(output);
    }

    /**
     * This method allows for testing the password logic
     */
    public static void main(String[] args) {
        assertPasswordMeetsRequirements(null, false);
        assertPasswordMeetsRequirements("", false);
        assertPasswordMeetsRequirements("!Short!", false);
        assertPasswordMeetsRequirements("Short+OK", true);
        assertPasswordMeetsRequirements("IsMyNewPasswordTooLong?Yes", false);
        assertPasswordMeetsRequirements("IsMyNewPasswordTooLong?No", true);
        assertPasswordMeetsRequirements("abcd1234", false);
        assertPasswordMeetsRequirements("Abcd1234", true);
        assertPasswordMeetsRequirements("JustLettersOops", false);
        assertPasswordMeetsRequirements("GotSymbols!", true);
        assertPasswordMeetsRequirements("Pretty, but has SPACES!", false);
        assertPasswordMeetsRequirements("This-is-a-[TAB]:\t(yuck)", false);
        assertPasswordMeetsRequirements("Everything+But+Digits", true);
        assertPasswordMeetsRequirements("no-upper-case-1", true);
        assertPasswordMeetsRequirements("NO-LOWER-CASE-1", true);
        assertPasswordMeetsRequirements("This-has.all'4^THINGS!!!", true);
        assertPasswordMeetsRequirements("NOT.DIVERSE", false);
        assertPasswordMeetsRequirements("not-diverse", false);
        assertPasswordMeetsRequirements("QBERT:@$%&@#!", false);
        assertPasswordMeetsRequirements("JUST2BORING", false);
        assertPasswordMeetsRequirements("just2boring", false);
        assertPasswordMeetsRequirements("1!2@3#4$5%6^7&8*9(0)", false);
        assertPasswordMeetsRequirements("Password1", true);
        assertPasswordMeetsRequirements("Look/Some\\Slashes!", false);
        assertPasswordMeetsRequirements("ForwardSlash/BAD!", false);
        assertPasswordMeetsRequirements("Backslash\\BAD!", false);
    }

}
