package chorestogetherapiservice.util;

/**
 * Util for getting environment variables.
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public class EnvValReader {

  public static String getStage() {
    return System.getenv("STAGE").toLowerCase();
  }

  public static String getRegion() {
    return System.getenv("AWS_REGION").toLowerCase();
  }
}
