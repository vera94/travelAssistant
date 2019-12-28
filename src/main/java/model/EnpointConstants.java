package model;

public class EnpointConstants {
	public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIIN_UP_URL = "/user/signup";
    public static final String REGISTRATION_URL = "/user/register";
}
