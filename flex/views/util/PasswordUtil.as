package views.util {
import hci.flex.util.DictionaryManager;

import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;
import mx.collections.XMLListCollection;

    public class PasswordUtil {

        public static const REQUIREMENTS_TEXT:String =
                "Passwords must be 8-25 characters long, contain no spaces" + "\n" +
                "or slashes, and contain three or more of the following:" + "\n" +
                "lowercase letter, uppercase letter, digit, or symbol.";

        public static const MATCH_ERROR_TEXT:String = "The two passwords you provided do not match.";

        public static const COMPLEXITY_ERROR_TEXT:String = "The password you provided does not meet complexity requirements.";

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
        public static function passwordMeetsRequirements(password:String):Boolean {
            if (password == null) {
                return false;
            }
            if (password.length < 8 || password.length > 25) {
                return false;
            }
            var containsLowerCase:Boolean = false;
            var containsUpperCase:Boolean = false;
            var containsDigit:Boolean = false;
            var containsWhitespace:Boolean = false;
            var containsSlash:Boolean = false;
            var containsOther:Boolean = false;
            for (var i:int = 0; i < password.length; i++) {
                var single:String = password.charAt(i);
                if (/[a-z]/.test(single)) {
                    containsLowerCase = true;
                } else if (/[A-Z]/.test(single)) {
                    containsUpperCase = true;
                } else if (/[0-9]/.test(single)) {
                    containsDigit = true;
                } else if (/\s/.test(single)) {
                    containsWhitespace = true;
                } else if (/[\/\\]/.test(single)) {   // slash or backslash
                    containsSlash = true;
                } else {
                    containsOther = true;
                }
            }
            if (containsWhitespace || containsSlash) {
                return false;
            }
            var varietyCount:int = 0;
            varietyCount += containsLowerCase ? 1 : 0;
            varietyCount += containsUpperCase ? 1 : 0;
            varietyCount += containsDigit ? 1 : 0;
            varietyCount += containsOther ? 1 : 0;
            return (varietyCount >= 3);
        }

    }
}