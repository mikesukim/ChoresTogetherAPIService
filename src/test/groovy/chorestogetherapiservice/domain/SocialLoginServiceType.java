package chorestogetherapiservice.domain;

/**
 * This Enum should be only used for testing purpose.
 * This Enum overloads SocialLoginServiceType enum from source file,
 * to have a extra enum value, NOT_IMPLEMENTED_SOCIAL_SERVICE, to be used for testing.
 * <a href="https://stackoverflow.com/a/59802629">source</a>
 * */
public enum SocialLoginServiceType {
  GOOGLE, KAKAO, NOT_IMPLEMENTED_SOCIAL_SERVICE
}
