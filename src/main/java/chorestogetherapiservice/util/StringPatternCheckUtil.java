package chorestogetherapiservice.util;

import java.util.regex.Pattern;

/**
 * Util for checking String pattern match through regex.
 */
public class StringPatternCheckUtil {
  public static boolean checkEmailPattern(String s) {
    String emailRegex = "[\\w-]+@([\\w-]+\\.)+[\\w-]+";
    return Pattern.matches(emailRegex, s);
  }
}
