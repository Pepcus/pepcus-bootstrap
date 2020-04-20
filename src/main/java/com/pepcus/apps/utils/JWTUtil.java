package com.pepcus.apps.utils;

import static com.pepcus.apps.constant.ApplicationConstants.JWT_TOKEN_ISS;
import static com.pepcus.apps.constant.ApplicationConstants.JWT_TOKEN_ROLE;
import static com.pepcus.apps.constant.ApplicationConstants.JWT_TOKEN_SUB;
import static com.pepcus.apps.constant.ApplicationConstants.JWT_TOKEN_USER;
import java.util.Map;
import com.pepcus.apps.model.AppAuthData;


/**
 * Utility class to decode a JWT token
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public class JWTUtil {

  /**
   * Get the values from map and set it to AppAuthToken
   * 
   * @param claims
   * @return
   */
  public static AppAuthData prepareAuthToken(Map<String, ?> tokenMap) {
    Object sub = tokenMap.get(JWT_TOKEN_SUB);
    Object user = tokenMap.get(JWT_TOKEN_USER);
    Object issClaim = tokenMap.get(JWT_TOKEN_ISS);
    Object role = tokenMap.get(JWT_TOKEN_ROLE);

    AppAuthData authToken = new AppAuthData();
    if (user != null) {
      authToken.setUser((String) user);
    }
    if (issClaim != null) {
      authToken.setIss((String) issClaim);
    }
    if (role != null) {
      authToken.setRole((String) role);
    }
    if (sub != null) {
      authToken.setSub((Integer) sub);
      authToken.setUserId((Integer) sub);
    }
    return authToken;
  }


}
