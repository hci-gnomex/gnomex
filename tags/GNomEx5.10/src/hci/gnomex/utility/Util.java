package hci.gnomex.utility;

public class Util {

  // Parses a comma delimited string where commas are ignored if between quotes.
  public static String[] parseCommaDelimited(String s) {
    if (s == null) {
      return new String[0];
    } else {
      String otherThanQuote = " [^\"] ";
      String quotedString = String.format(" \" %s* \" ", otherThanQuote);
      String regex = String.format("(?x) "+ // enable comments, ignore white spaces
          ",                         "+ // match a comma
          "(?=                       "+ // start positive look ahead
          "  (                       "+ //   start group 1
          "    %s*                   "+ //     match 'otherThanQuote' zero or more times
          "    %s                    "+ //     match 'quotedString'
          "  )*                      "+ //   end group 1 and repeat it zero or more times
          "  %s*                     "+ //   match 'otherThanQuote'
          "  $                       "+ // match the end of the string
          ")                         ", // stop positive look ahead
          otherThanQuote, quotedString, otherThanQuote);
  
      String[] tokens = s.split(regex);
  
      return tokens;
    }
  }
}
